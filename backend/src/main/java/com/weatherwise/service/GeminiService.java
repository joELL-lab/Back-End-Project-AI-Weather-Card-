package com.weatherwise.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherwise.dto.AiRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 2000;

    public Object getAiSuggestion(AiRequest request) {
        String url = apiUrl + "?key=" + apiKey;

        Map<String, Object> body = Map.of(
            "system_instruction", Map.of(
                "parts", List.of(Map.of("text", request.getSystemMessage()))
            ),
            "contents", List.of(
                Map.of(
                    "role", "user",
                    "parts", List.of(Map.of("text", request.getUserMessage()))
                )
            ),
            "generationConfig", Map.of(
                "responseMimeType", "application/json"
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Retry loop for transient errors (429 rate limiting)
        for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Gemini API error: " + response.getStatusCode());
                }

                JsonNode root = objectMapper.readTree(response.getBody());
                String text = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

                return objectMapper.readValue(text, Object.class);

            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    if (attempt < MAX_RETRIES) {
                        long backoff = INITIAL_BACKOFF_MS * (long) Math.pow(2, attempt);
                        log.warn("Gemini API rate limited (429). Retrying in {}ms (attempt {}/{})",
                                backoff, attempt + 1, MAX_RETRIES);
                        try {
                            Thread.sleep(backoff);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException("AI request was interrupted during retry backoff.");
                        }
                    } else {
                        log.error("Gemini API rate limit exceeded after {} retries", MAX_RETRIES);
                        throw new RuntimeException(
                            "AI quota exceeded. The free tier limit has been reached. Please try again in a few minutes.");
                    }
                } else {
                    log.error("Gemini API client error: {} - {}", e.getStatusCode(), e.getStatusText());
                    throw new RuntimeException(
                        "AI service error (HTTP " + e.getStatusCode().value() + "). Please try again later.");
                }
            } catch (HttpServerErrorException e) {
                log.error("Gemini API server error: {} - {}", e.getStatusCode(), e.getStatusText());
                throw new RuntimeException(
                    "AI service is temporarily unavailable. Please try again later.");
            } catch (RuntimeException e) {
                // Re-throw our own RuntimeExceptions (from parse failures, etc.)
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
            }
        }

        // Should not reach here, but just in case
        throw new RuntimeException("AI request failed after multiple attempts. Please try again later.");
    }
}
