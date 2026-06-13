package com.accessiblity.backend.service;

import com.accessiblity.backend.entity.AccessibilityStatus;
import com.accessiblity.backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for dashboard analytics and statistics.
 * Uses accessibility status enum for metrics calculation.
 *
 * TODO: Integrate with analytics team's calculations and sample datasets
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PlaceRepository placeRepository;
    private final AccessibilityScoreService scoreService;

    /**
     * Get total number of places in the system.
     * TODO: Integrate with analytics team's data aggregation layer.
     *
     * @return total places count
     */
    public Long getTotalPlaces() {
        return placeRepository.count();
    }

    /**
     * Get count of accessible places (score >= 80).
     * Updated to use new accessibility_status enum classification.
     *
     * @return count of accessible places
     */
    public Long getAccessiblePlaces() {
        return placeRepository.countByAccessibilityStatus(AccessibilityStatus.ACCESSIBLE);
    }

    /**
     * Get count of partially accessible places (score 50-79).
     * Updated to use new accessibility_status enum classification.
     *
     * @return count of partially accessible places
     */
    public Long getPartiallyAccessiblePlaces() {
        return placeRepository.countByAccessibilityStatus(AccessibilityStatus.PARTIAL);
    }

    /**
     * Get count of not accessible places (score < 50).
     * Updated to use new accessibility_status enum classification.
     *
     * @return count of not accessible places
     */
    public Long getNotAccessiblePlaces() {
        return placeRepository.countByAccessibilityStatus(AccessibilityStatus.NOT_ACCESSIBLE);
    }

    /**
     * Get average accessibility score across all places.
     * TODO: Replace with analytics team's final average calculation.
     * TODO: Integrate with dashboard metrics calculations.
     * TODO: Handle edge cases (null scores, empty datasets).
     *
     * @return average accessibility score
     */
    public Double getAverageAccessibilityScore() {
        // Placeholder: Returns average of all place scores
        // TODO: Replace with analytics team's aggregation function
        return placeRepository.findAll().stream()
                .mapToInt(place -> place.getAccessibilityScore() != null ? place.getAccessibilityScore() : 0)
                .average()
                .orElse(0.0);
    }
}
