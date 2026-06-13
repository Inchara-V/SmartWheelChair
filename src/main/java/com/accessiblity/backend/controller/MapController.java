package com.accessiblity.backend.controller;

import com.accessiblity.backend.dto.HeatmapPointDTO;
import com.accessiblity.backend.dto.MapMarkerDTO;
import com.accessiblity.backend.dto.NearbyPlaceDTO;
import com.accessiblity.backend.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for map-related endpoints.
 * Provides APIs for map markers, nearby places, and heatmap data.
 */
@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    /**
     * Get all places as map markers.
     * 
     * Endpoint: GET /api/places/map
     *
     * @return list of map markers with accessibility information
     */
    @GetMapping("/map")
    public ResponseEntity<List<MapMarkerDTO>> getMapMarkers() {
        List<MapMarkerDTO> markers = mapService.getMapMarkers();
        return ResponseEntity.ok(markers);
    }

    /**
     * Get nearby accessible places within a specified radius.
     * 
     * Endpoint: GET /api/places/nearby
     *
     * @param latitude user's current latitude
     * @param longitude user's current longitude
     * @param radius search radius in kilometers (default: 5 km)
     * @return list of nearby accessible places sorted by distance
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyPlaceDTO>> getNearbyAccessiblePlaces(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(value = "radius", required = false) Double radius) {
        List<NearbyPlaceDTO> nearbyPlaces = mapService.getNearbyAccessiblePlaces(lat, lng, radius);
        return ResponseEntity.ok(nearbyPlaces);
    }
}
