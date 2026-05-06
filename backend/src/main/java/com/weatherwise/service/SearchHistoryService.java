package com.weatherwise.service;

import com.weatherwise.model.SearchHistory;
import com.weatherwise.model.User;
import com.weatherwise.repository.SearchHistoryRepository;
import com.weatherwise.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository historyRepository;
    private final UserRepository userRepository;

    public List<Map<String, Object>> getHistory(String username) {
        User user = getUser(username);
        return historyRepository.findByUserIdOrderBySearchedAtDesc(user.getId())
                .stream()
                .map(h -> Map.<String, Object>of(
                        "id", h.getId(),
                        "cityName", h.getCityName(),
                        "country", h.getCountry() != null ? h.getCountry() : "",
                        "searchedAt", h.getSearchedAt().toString()
                ))
                .collect(Collectors.toList());
    }

    public Map<String, Object> addHistory(String username, String cityName, String country) {
        User user = getUser(username);

        SearchHistory history = SearchHistory.builder()
                .user(user)
                .cityName(cityName)
                .country(country)
                .build();

        SearchHistory saved = historyRepository.save(history);
        return Map.of(
                "id", saved.getId(),
                "cityName", saved.getCityName(),
                "country", saved.getCountry() != null ? saved.getCountry() : "",
                "searchedAt", saved.getSearchedAt().toString()
        );
    }

    public void deleteOne(String username, Long id) {
        User user = getUser(username);
        SearchHistory history = historyRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Riwayat tidak ditemukan."));
        historyRepository.delete(history);
    }

    @Transactional
    public void deleteAll(String username) {
        User user = getUser(username);
        historyRepository.deleteByUserId(user.getId());
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan."));
    }
}
