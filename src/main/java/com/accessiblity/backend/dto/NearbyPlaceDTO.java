package com.accessiblity.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for nearby accessible places.
 * Includes distance from user's current location.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbyPlaceDTO {
    private Long placeId;
    private String placeName;
    private Double distance;
    private Integer accessibilityScore;
    private String accessibilityCategory;
    private Double latitude;
    private Double longitude;
    private String address;
}
