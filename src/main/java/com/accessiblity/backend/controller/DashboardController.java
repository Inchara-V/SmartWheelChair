package com.accessiblity.backend.controller;

import com.accessiblity.backend.dto.DashboardStatsResponse;
import com.accessiblity.backend.dto.HeatmapPointDTO;
import com.accessiblity.backend.service.DashboardService;
import com.accessiblity.backend.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for dashboard analytics endpoints.
 * Provides aggregated statistics for the accessibility dashboard.
 *
 * TODO: Integrate with analytics team's additional reporting requirements
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final MapService mapService;

    /**
     * Get dashboard statistics.
     * Returns aggregated counts and metrics for dashboard display.
     *
     * TODO: Add additional metrics when analytics team provides requirements
     * Extension point for future dashboard metric additions
     *
     * @return dashboard statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        DashboardStatsResponse stats = DashboardStatsResponse.builder()
                .totalPlaces(dashboardService.getTotalPlaces())
                .accessiblePlaces(dashboardService.getAccessiblePlaces())
                .partiallyAccessiblePlaces(dashboardService.getPartiallyAccessiblePlaces())
                .notAccessiblePlaces(dashboardService.getNotAccessiblePlaces())
                .averageAccessibilityScore(dashboardService.getAverageAccessibilityScore())
                .build();

        return ResponseEntity.ok(stats);
    }

    /**
     * Get heatmap data for accessibility visualization.
     * Returns raw accessibility scores with geographic coordinates.
     * 
     * Endpoint: GET /api/dashboard/heatmap
     *
     * @return list of heatmap points with accessibility scores
     */
    @GetMapping("/heatmap")
    public ResponseEntity<List<HeatmapPointDTO>> getHeatmapData() {
        List<HeatmapPointDTO> heatmapData = mapService.getHeatmapData();
        return ResponseEntity.ok(heatmapData);
    }
}
