package com.weatherwise.controller;

import com.weatherwise.dto.LocationDTO;
import com.weatherwise.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getLocations(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(locationService.getLocations(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<?> saveLocation(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody LocationDTO dto) {
        try {
            LocationDTO saved = locationService.saveLocation(userDetails.getUsername(), dto);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        try {
            locationService.deleteLocation(userDetails.getUsername(), id);
            return ResponseEntity.ok(Map.of("message", "Lokasi berhasil dihapus."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
