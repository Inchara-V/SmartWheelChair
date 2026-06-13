package com.accessiblity.backend.controller;

import com.accessiblity.backend.dto.PlaceDTO;
import com.accessiblity.backend.service.AccessibilityScoreService;
import com.accessiblity.backend.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for accessibility score endpoints.
 * Provides detailed score information for places.
 */
@RestController
@RequestMapping("/api/score")
@RequiredArgsConstructor
public class ScoreController {

    private final PlaceService placeService;
    private final AccessibilityScoreService scoreService;

    /**
     * Get accessibility score and classification for a place.
     *
     * @param placeId the place ID
     * @return score details including classification
     */
    @GetMapping("/place/{placeId}")
    public ResponseEntity<Map<String, Object>> getPlaceScore(@PathVariable Long placeId) {
        PlaceDTO placeDTO = placeService.getPlaceById(placeId);

        // Determine accessibility classification
        String classification;
        if (scoreService.isFullyAccessible(convertToEntity(placeDTO))) {
            classification = "FULLY_ACCESSIBLE";
        } else if (scoreService.isPartiallyAccessible(convertToEntity(placeDTO))) {
            classification = "PARTIALLY_ACCESSIBLE";
        } else {
            classification = "NOT_ACCESSIBLE";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("placeId", placeDTO.getId());
        response.put("placeName", placeDTO.getName());
        response.put("score", placeDTO.getAccessibilityScore());
        response.put("classification", classification);
        response.put("features", Map.of(
                "hasRamp", placeDTO.getHasRamp(),
                "hasElevator", placeDTO.getHasElevator(),
                "hasAccessibleToilet", placeDTO.getHasAccessibleToilet(),
                "hasWheelchairAccess", placeDTO.getHasWheelchairAccess()
        ));

        return ResponseEntity.ok(response);
    }

    /**
     * Convert PlaceDTO to Place entity for scoring.
     * Helper method for internal use.
     *
     * @param placeDTO the DTO
     * @return the entity
     */
    private com.accessiblity.backend.entity.Place convertToEntity(PlaceDTO placeDTO) {
        return com.accessiblity.backend.entity.Place.builder()
                .id(placeDTO.getId())
                .name(placeDTO.getName())
                .address(placeDTO.getAddress())
                .latitude(placeDTO.getLatitude())
                .longitude(placeDTO.getLongitude())
                .description(placeDTO.getDescription())
                .placeType(placeDTO.getPlaceType())
                .hasRamp(placeDTO.getHasRamp())
                .hasElevator(placeDTO.getHasElevator())
                .hasAccessibleToilet(placeDTO.getHasAccessibleToilet())
                .hasWheelchairAccess(placeDTO.getHasWheelchairAccess())
                .accessibilityScore(placeDTO.getAccessibilityScore())
                .createdAt(placeDTO.getCreatedAt())
                .updatedAt(placeDTO.getUpdatedAt())
                .build();
    }
}
