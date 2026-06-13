package com.accessiblity.backend.service;

import com.accessiblity.backend.dto.PlaceDTO;
import com.accessiblity.backend.entity.Place;
import com.accessiblity.backend.exception.ResourceNotFoundException;
import com.accessiblity.backend.exception.ValidationException;
import com.accessiblity.backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final AccessibilityScoreService scoreService;

    /**
     * Get all places.
     *
     * @return list of all places
     */
    public List<PlaceDTO> getAllPlaces() {
        return placeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get place by ID.
     *
     * @param id the place ID
     * @return place details
     * @throws ResourceNotFoundException if place not found
     */
    public PlaceDTO getPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with id: " + id));
        return convertToDTO(place);
    }

    /**
     * Create a new place and calculate its accessibility score.
     *
     * @param placeDTO the place data
     * @return created place details
     * @throws ValidationException if place data is invalid
     */
    public PlaceDTO createPlace(PlaceDTO placeDTO) {
        validatePlaceDTO(placeDTO);

        Place place = convertToEntity(placeDTO);

        // Calculate accessibility score using the dedicated scoring service
        int score = scoreService.calculateScore(place);
        place.setAccessibilityScore(score);
        
        // Calculate accessibility status based on score
        place.setAccessibilityStatus(scoreService.calculateAccessibilityStatus(score));

        Place savedPlace = placeRepository.save(place);
        return convertToDTO(savedPlace);
    }

    /**
     * Update an existing place and recalculate its accessibility score.
     *
     * @param id      the place ID to update
     * @param placeDTO the updated place data
     * @return updated place details
     * @throws ResourceNotFoundException if place not found
     * @throws ValidationException      if place data is invalid
     */
    public PlaceDTO updatePlace(Long id, PlaceDTO placeDTO) {
        validatePlaceDTO(placeDTO);

        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with id: " + id));

        // Update place fields
        place.setName(placeDTO.getName());
        place.setAddress(placeDTO.getAddress());
        place.setLatitude(placeDTO.getLatitude());
        place.setLongitude(placeDTO.getLongitude());
        place.setDescription(placeDTO.getDescription());
        place.setPlaceType(placeDTO.getPlaceType());
        place.setHasRamp(placeDTO.getHasRamp() != null ? placeDTO.getHasRamp() : false);
        place.setHasElevator(placeDTO.getHasElevator() != null ? placeDTO.getHasElevator() : false);
        place.setHasAccessibleToilet(placeDTO.getHasAccessibleToilet() != null ? placeDTO.getHasAccessibleToilet() : false);
        place.setHasWheelchairAccess(placeDTO.getHasWheelchairAccess() != null ? placeDTO.getHasWheelchairAccess() : false);
        place.setHasAccessibleParking(placeDTO.getHasAccessibleParking() != null ? placeDTO.getHasAccessibleParking() : false);

        // Recalculate accessibility score
        int score = scoreService.calculateScore(place);
        place.setAccessibilityScore(score);
        
        // Recalculate accessibility status
        place.setAccessibilityStatus(scoreService.calculateAccessibilityStatus(score));

        Place updatedPlace = placeRepository.save(place);
        return convertToDTO(updatedPlace);
    }

    /**
     * Delete a place by ID.
     *
     * @param id the place ID
     * @throws ResourceNotFoundException if place not found
     */
    public void deletePlace(Long id) {
        if (!placeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Place not found with id: " + id);
        }
        placeRepository.deleteById(id);
    }

    /**
     * Validate place DTO.
     *
     * @param placeDTO the place to validate
     * @throws ValidationException if validation fails
     */
    private void validatePlaceDTO(PlaceDTO placeDTO) {
        if (placeDTO.getName() == null || placeDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Place name is required");
        }
        if (placeDTO.getAddress() == null || placeDTO.getAddress().trim().isEmpty()) {
            throw new ValidationException("Place address is required");
        }
        if (placeDTO.getPlaceType() == null) {
            throw new ValidationException("Place type is required");
        }
    }

    /**
     * Convert Place entity to PlaceDTO.
     *
     * @param place the place entity
     * @return the DTO
     */
    private PlaceDTO convertToDTO(Place place) {
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .description(place.getDescription())
                .placeType(place.getPlaceType())
                .hasRamp(place.getHasRamp())
                .hasElevator(place.getHasElevator())
                .hasAccessibleToilet(place.getHasAccessibleToilet())
                .hasWheelchairAccess(place.getHasWheelchairAccess())
                .hasAccessibleParking(place.getHasAccessibleParking())
                .accessibilityScore(place.getAccessibilityScore())
                .accessibilityStatus(place.getAccessibilityStatus() != null ? 
                    place.getAccessibilityStatus().name() : null)
                .imageUrl(place.getImageUrl())
                .createdAt(place.getCreatedAt())
                .updatedAt(place.getUpdatedAt())
                .build();
    }

    /**
     * Convert PlaceDTO to Place entity.
     *
     * @param placeDTO the DTO
     * @return the entity
     */
    private Place convertToEntity(PlaceDTO placeDTO) {
        return Place.builder()
                .name(placeDTO.getName())
                .address(placeDTO.getAddress())
                .latitude(placeDTO.getLatitude())
                .longitude(placeDTO.getLongitude())
                .description(placeDTO.getDescription())
                .placeType(placeDTO.getPlaceType())
                .hasRamp(placeDTO.getHasRamp() != null ? placeDTO.getHasRamp() : false)
                .hasElevator(placeDTO.getHasElevator() != null ? placeDTO.getHasElevator() : false)
                .hasAccessibleToilet(placeDTO.getHasAccessibleToilet() != null ? placeDTO.getHasAccessibleToilet() : false)
                .hasWheelchairAccess(placeDTO.getHasWheelchairAccess() != null ? placeDTO.getHasWheelchairAccess() : false)
                .hasAccessibleParking(placeDTO.getHasAccessibleParking() != null ? placeDTO.getHasAccessibleParking() : false)
                .imageUrl(placeDTO.getImageUrl())
                .build();
    }
}
