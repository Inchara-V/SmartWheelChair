package com.accessiblity.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for heatmap data points.
 * Contains raw accessibility score for heatmap visualization.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeatmapPointDTO {
    private Double latitude;
    private Double longitude;
    private Integer score;
}
