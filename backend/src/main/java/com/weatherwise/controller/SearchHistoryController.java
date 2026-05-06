package com.weatherwise.controller;

import com.weatherwise.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService historyService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(historyService.getHistory(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<?> addHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> body) {
        try {
            String cityName = body.get("cityName");
            String country = body.get("country");
            if (cityName == null || cityName.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("message", "cityName wajib diisi."));
            }
            Map<String, Object> saved = historyService.addHistory(userDetails.getUsername(), cityName, country);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        try {
            historyService.deleteOne(userDetails.getUsername(), id);
            return ResponseEntity.ok(Map.of("message", "Riwayat berhasil dihapus."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(
            @AuthenticationPrincipal UserDetails userDetails) {
        historyService.deleteAll(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Semua riwayat berhasil dihapus."));
    }
}
