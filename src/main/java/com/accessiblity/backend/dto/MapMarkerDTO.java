package com.accessiblity.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for map markers.
 * Contains minimal data needed for displaying places on a map with marker customization.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapMarkerDTO {
    private Long placeId;
    private String placeName;
    private Double latitude;
    private Double longitude;
    private Integer accessibilityScore;
    private String accessibilityCategory;
    private Boolean rampAvailable;
    private Boolean elevatorAvailable;
    private Boolean accessibleWashroom;
    private String address;
}
