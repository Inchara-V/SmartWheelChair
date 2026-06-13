package com.accessiblity.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.accessiblity.backend.dto.HeatmapPointDTO;
import com.accessiblity.backend.dto.MapMarkerDTO;
import com.accessiblity.backend.dto.NearbyPlaceDTO;
import com.accessiblity.backend.entity.Place;
import com.accessiblity.backend.repository.PlaceRepository;
import com.accessiblity.backend.util.HaversineUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service for map-related operations including marker data and nearby places.
 */
@Service
@RequiredArgsConstructor
public class MapService {

    private final PlaceRepository placeRepository;

    /**
     * Get all places as map markers.
     *
     * @return list of map markers
     */
    public List<MapMarkerDTO> getMapMarkers() {
        return placeRepository.findAll().stream()
                .map(this::convertToMapMarker)
                .collect(Collectors.toList());
    }

    /**
     * Get nearby accessible places within a specified radius.
     */
    public List<NearbyPlaceDTO> getNearbyAccessiblePlaces(Double latitude, Double longitude, Double radiusKm) {
        final Double finalRadius = (radiusKm == null || radiusKm <= 0) ? 5.0 : radiusKm;

        List<Place> accessiblePlaces = placeRepository.findByAccessibilityScoreGreaterThanEqualOrderByAccessibilityScoreDesc(80);

        return accessiblePlaces.stream()
                .filter(place -> HaversineUtil.isWithinRadius(latitude, longitude, place.getLatitude(), place.getLongitude(), finalRadius))
                .map(place -> {
                    NearbyPlaceDTO dto = new NearbyPlaceDTO();
                    dto.setPlaceId(place.getId());
                    dto.setPlaceName(place.getName());
                    dto.setDistance(HaversineUtil.calculateDistance(latitude, longitude, place.getLatitude(), place.getLongitude()));
                    dto.setAccessibilityScore(place.getAccessibilityScore());
                    // Fixed: Safely maps placeType enum name to your DTO string field
                    dto.setAccessibilityCategory(place.getPlaceType() != null ? place.getPlaceType().name() : "UNKNOWN");
                    dto.setLatitude(place.getLatitude());
                    dto.setLongitude(place.getLongitude());
                    dto.setAddress(place.getAddress());
                    return dto;
                })
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .collect(Collectors.toList());
    }

    /**
     * Get heatmap data points for all places with accessibility scores.
     */
    public List<HeatmapPointDTO> getHeatmapData() {
        return placeRepository.findAll().stream()
                .filter(place -> place.getLatitude() != null && place.getLongitude() != null && place.getAccessibilityScore() != null)
                .map(place -> {
                    HeatmapPointDTO dto = new HeatmapPointDTO();
                    dto.setLatitude(place.getLatitude());
                    dto.setLongitude(place.getLongitude());
                    dto.setScore(place.getAccessibilityScore());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convert Place entity to MapMarkerDTO.
     */
    private MapMarkerDTO convertToMapMarker(Place place) {
        MapMarkerDTO dto = new MapMarkerDTO();
        dto.setPlaceId(place.getId());
        dto.setPlaceName(place.getName());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setAccessibilityScore(place.getAccessibilityScore());
        // Fixed: Safely maps placeType enum name to your DTO string field
        dto.setAccessibilityCategory(place.getPlaceType() != null ? place.getPlaceType().name() : "UNKNOWN");
        dto.setRampAvailable(place.getHasRamp());
        dto.setElevatorAvailable(place.getHasElevator());
        dto.setAccessibleWashroom(place.getHasAccessibleToilet());
        dto.setAddress(place.getAddress());
        return dto;
    }
}
