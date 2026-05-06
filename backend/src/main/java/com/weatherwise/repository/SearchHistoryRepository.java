package com.weatherwise.repository;

import com.weatherwise.model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserIdOrderBySearchedAtDesc(Long userId);
    Optional<SearchHistory> findByIdAndUserId(Long id, Long userId);
    void deleteByUserId(Long userId);
}
