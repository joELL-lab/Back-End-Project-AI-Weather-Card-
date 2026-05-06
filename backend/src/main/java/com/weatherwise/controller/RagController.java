package com.weatherwise.controller;

import com.weatherwise.dto.RagRequest;
import com.weatherwise.dto.RagResponse;
import com.weatherwise.model.KnowledgeDocument;
import com.weatherwise.repository.KnowledgeDocumentRepository;
import com.weatherwise.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;
    private final KnowledgeDocumentRepository documentRepository;

    /**
     * RAG endpoint - ask a question with context retrieval
     */
    @PostMapping("/ask")
    public ResponseEntity<?> askQuestion(@RequestBody RagRequest request) {
        try {
            if (request.getQuestion() == null || request.getQuestion().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Pertanyaan tidak boleh kosong."));
            }
            RagResponse response = ragService.askQuestion(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Gagal memproses pertanyaan: " + e.getMessage()));
        }
    }

    /**
     * Get all knowledge documents (for admin/debug)
     */
    @GetMapping("/documents")
    public ResponseEntity<List<KnowledgeDocument>> getDocuments() {
        return ResponseEntity.ok(documentRepository.findAll());
    }

    /**
     * Add a new knowledge document
     */
    @PostMapping("/documents")
    public ResponseEntity<?> addDocument(@RequestBody KnowledgeDocument document) {
        try {
            KnowledgeDocument saved = documentRepository.save(document);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Gagal menambah dokumen: " + e.getMessage()));
        }
    }

    /**
     * Search documents by keyword
     */
    @GetMapping("/documents/search")
    public ResponseEntity<List<KnowledgeDocument>> searchDocuments(@RequestParam String q) {
        return ResponseEntity.ok(documentRepository.searchByKeyword(q));
    }
}
