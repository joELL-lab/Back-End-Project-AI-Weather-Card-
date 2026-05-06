package com.weatherwise.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherwise.dto.RagRequest;
import com.weatherwise.dto.RagResponse;
import com.weatherwise.model.KnowledgeDocument;
import com.weatherwise.repository.KnowledgeDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagService {

    private final KnowledgeDocumentRepository documentRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    /**
     * RAG Pipeline:
     * 1. Extract keywords from user question
     * 2. Retrieve relevant documents from MySQL
     * 3. Build context from retrieved documents
     * 4. Send context + question to Gemini
     * 5. Return augmented response
     */
    public RagResponse askQuestion(RagRequest request) {
        // Step 1: Extract keywords from question
        List<String> keywords = extractKeywords(request.getQuestion());

        // Add weather condition and city as keywords if provided
        if (request.getWeatherCondition() != null && !request.getWeatherCondition().isBlank()) {
            keywords.add(request.getWeatherCondition().toLowerCase());
        }
        if (request.getCity() != null && !request.getCity().isBlank()) {
            keywords.add(request.getCity().toLowerCase());
        }

        // Step 2: Retrieve relevant documents
        List<KnowledgeDocument> relevantDocs = retrieveDocuments(keywords);

        // Step 3: Build context string
        String context = buildContext(relevantDocs);

        // Step 4: Generate answer with Gemini using RAG context
        String answer = generateAnswer(request.getQuestion(), context);

        // Step 5: Build response with sources
        List<String> sources = relevantDocs.stream()
                .map(doc -> "[" + doc.getCategory() + "] " + doc.getTitle())
                .distinct()
                .collect(Collectors.toList());

        return RagResponse.builder()
                .answer(answer)
                .sources(sources)
                .documentsUsed(relevantDocs.size())
                .build();
    }

    private List<String> extractKeywords(String question) {
        // Simple keyword extraction: split and filter stop words
        Set<String> stopWords = Set.of(
            "apa", "bagaimana", "mengapa", "kenapa", "dimana", "kapan",
            "siapa", "yang", "dan", "atau", "di", "ke", "dari", "untuk",
            "dengan", "adalah", "ini", "itu", "saya", "kamu", "kita",
            "the", "what", "how", "why", "when", "where", "is", "are",
            "a", "an", "in", "on", "at", "to", "for", "of", "with",
            "do", "does", "can", "should", "would", "could", "if",
            "saat", "jika", "apakah", "cuaca", "weather", "tips", "tip"
        );

        return Arrays.stream(question.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .split("\\s+"))
                .filter(word -> word.length() > 2 && !stopWords.contains(word))
                .distinct()
                .collect(Collectors.toList());
    }

    private List<KnowledgeDocument> retrieveDocuments(List<String> keywords) {
        Set<KnowledgeDocument> results = new LinkedHashSet<>();

        for (String keyword : keywords) {
            List<KnowledgeDocument> docs = documentRepository.searchByKeyword(keyword);
            results.addAll(docs);

            // Limit to top 5 most relevant documents
            if (results.size() >= 5) break;
        }

        return new ArrayList<>(results).subList(0, Math.min(results.size(), 5));
    }

    private String buildContext(List<KnowledgeDocument> documents) {
        if (documents.isEmpty()) {
            return "Tidak ada dokumen referensi yang ditemukan.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== KONTEKS DARI KNOWLEDGE BASE ===\n\n");

        for (int i = 0; i < documents.size(); i++) {
            KnowledgeDocument doc = documents.get(i);
            sb.append("--- Dokumen ").append(i + 1).append(" ---\n");
            sb.append("Kategori: ").append(doc.getCategory()).append("\n");
            sb.append("Judul: ").append(doc.getTitle()).append("\n");
            sb.append("Isi: ").append(doc.getContent()).append("\n\n");
        }

        return sb.toString();
    }

    private String generateAnswer(String question, String context) {
        String url = apiUrl + "?key=" + apiKey;

        String systemPrompt = """
            Kamu adalah asisten cuaca cerdas bernama WeatherWise AI.
            Kamu menjawab pertanyaan tentang cuaca, kesehatan, aktivitas, dan tips berdasarkan konteks yang diberikan.
            
            ATURAN:
            1. Jawab berdasarkan informasi dari konteks yang diberikan (knowledge base)
            2. Jika konteks tidak mencukupi, kamu boleh menambahkan pengetahuanmu sendiri tapi sebutkan bahwa itu dari pengetahuan umum
            3. Jawab dengan bahasa yang ramah, kasual, dan informatif
            4. Gunakan emoji untuk membuat jawaban lebih menarik
            5. Jawab dalam bahasa yang sama dengan pertanyaan pengguna
            6. Berikan jawaban yang ringkas tapi lengkap (maksimal 3-4 paragraf)
            """;

        String userPrompt = context + "\n\n=== PERTANYAAN PENGGUNA ===\n" + question;

        Map<String, Object> body = Map.of(
            "system_instruction", Map.of(
                "parts", List.of(Map.of("text", systemPrompt))
            ),
            "contents", List.of(
                Map.of(
                    "role", "user",
                    "parts", List.of(Map.of("text", userPrompt))
                )
            ),
            "generationConfig", Map.of(
                "temperature", 0.7,
                "maxOutputTokens", 1024
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root
                .path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();
        } catch (Exception e) {
            log.error("Failed to get Gemini response: {}", e.getMessage());
            return "Maaf, terjadi kesalahan saat memproses pertanyaan kamu. Coba lagi nanti ya! 😔";
        }
    }
}
