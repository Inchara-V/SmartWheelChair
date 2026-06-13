package com.accessiblity.backend.service;

import com.accessiblity.backend.entity.AccessibilityStatus;
import com.accessiblity.backend.entity.Place;
import org.springframework.stereotype.Service;

/**
 * Service responsible for calculating accessibility scores.
 * All scoring logic is centralized here to enable easy replacement
 * when the analytics team provides the final formula.
 *
 * SCORING FORMULA (Updated 2026-06-13):
 * - Wheelchair Access: 30 points
 * - Ramp: 25 points
 * - Elevator: 20 points
 * - Accessible Toilet: 15 points
 * - Accessible Parking: 10 points
 * MAX: 100 points
 */
@Service
public class AccessibilityScoreService {

    // Updated scoring constants - final formula
    private static final int WHEELCHAIR_ACCESS_POINTS = 30;
    private static final int RAMP_POINTS = 25;
    private static final int ELEVATOR_POINTS = 20;
    private static final int ACCESSIBLE_TOILET_POINTS = 15;
    private static final int ACCESSIBLE_PARKING_POINTS = 10;

    /**
     * Calculate accessibility score for a place based on available facilities.
     * NEW FORMULA: Wheelchair (30) + Ramp (25) + Elevator (20) + Toilet (15) + Parking (10)
     *
     * @param place the place to calculate score for
     * @return accessibility score (0-100)
     */
    public int calculateScore(Place place) {
        int score = 0;

        if (place.getHasWheelchairAccess() != null && place.getHasWheelchairAccess()) {
            score += WHEELCHAIR_ACCESS_POINTS;  // 30
        }
        if (place.getHasRamp() != null && place.getHasRamp()) {
            score += RAMP_POINTS;  // 25
        }
        if (place.getHasElevator() != null && place.getHasElevator()) {
            score += ELEVATOR_POINTS;  // 20
        }
        if (place.getHasAccessibleToilet() != null && place.getHasAccessibleToilet()) {
            score += ACCESSIBLE_TOILET_POINTS;  // 15
        }
        if (place.getHasAccessibleParking() != null && place.getHasAccessibleParking()) {
            score += ACCESSIBLE_PARKING_POINTS;  // 10
        }

        return Math.min(score, 100);
    }

    /**
     * Calculate accessibility status based on score.
     * ACCESSIBLE: score >= 80
     * PARTIAL: score 50-79
     * NOT_ACCESSIBLE: score < 50
     *
     * @param score the accessibility score
     * @return accessibility status enum
     */
    public AccessibilityStatus calculateAccessibilityStatus(int score) {
        if (score >= 80) {
            return AccessibilityStatus.ACCESSIBLE;
        } else if (score >= 50) {
            return AccessibilityStatus.PARTIAL;
        } else {
            return AccessibilityStatus.NOT_ACCESSIBLE;
        }
    }
}
