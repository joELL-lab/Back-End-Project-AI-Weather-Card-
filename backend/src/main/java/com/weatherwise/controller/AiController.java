package com.weatherwise.controller;

import com.weatherwise.dto.AiRequest;
import com.weatherwise.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final GeminiService geminiService;

    @PostMapping("/suggestions")
    public ResponseEntity<?> getSuggestions(@RequestBody AiRequest request) {
        try {
            Object result = geminiService.getAiSuggestion(request);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Gagal mendapatkan saran AI: " + e.getMessage()));
        }
    }
}
