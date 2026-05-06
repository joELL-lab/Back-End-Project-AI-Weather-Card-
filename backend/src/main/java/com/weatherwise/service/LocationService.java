package com.weatherwise.service;

import com.weatherwise.dto.LocationDTO;
import com.weatherwise.model.SavedLocation;
import com.weatherwise.model.User;
import com.weatherwise.repository.SavedLocationRepository;
import com.weatherwise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final SavedLocationRepository locationRepository;
    private final UserRepository userRepository;

    public List<LocationDTO> getLocations(String username) {
        User user = getUser(username);
        return locationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LocationDTO saveLocation(String username, LocationDTO dto) {
        User user = getUser(username);

        if (locationRepository.existsByUserIdAndLatAndLon(user.getId(), dto.getLat(), dto.getLon())) {
            throw new RuntimeException("Lokasi ini sudah tersimpan.");
        }

        SavedLocation location = SavedLocation.builder()
                .user(user)
                .name(dto.getName())
                .country(dto.getCountry())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();

        SavedLocation saved = locationRepository.save(location);
        return toDTO(saved);
    }

    public void deleteLocation(String username, Long locationId) {
        User user = getUser(username);
        SavedLocation location = locationRepository.findByIdAndUserId(locationId, user.getId())
                .orElseThrow(() -> new RuntimeException("Lokasi tidak ditemukan."));
        locationRepository.delete(location);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan."));
    }

    private LocationDTO toDTO(SavedLocation loc) {
        LocationDTO dto = new LocationDTO();
        dto.setId(loc.getId());
        dto.setName(loc.getName());
        dto.setCountry(loc.getCountry());
        dto.setLat(loc.getLat());
        dto.setLon(loc.getLon());
        dto.setCreatedAt(loc.getCreatedAt().toString());
        return dto;
    }
}
