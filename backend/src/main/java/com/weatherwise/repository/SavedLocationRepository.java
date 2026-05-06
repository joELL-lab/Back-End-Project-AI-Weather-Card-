package com.weatherwise.repository;

import com.weatherwise.model.SavedLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedLocationRepository extends JpaRepository<SavedLocation, Long> {
    List<SavedLocation> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<SavedLocation> findByIdAndUserId(Long id, Long userId);
    boolean existsByUserIdAndLatAndLon(Long userId, Double lat, Double lon);
}
