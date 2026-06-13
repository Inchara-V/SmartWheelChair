package com.accessiblity.backend.repository;

import com.accessiblity.backend.entity.AccessibilityStatus;
import com.accessiblity.backend.entity.Place;
import com.accessiblity.backend.entity.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByPlaceType(PlaceType placeType);

    List<Place> findByHasRampTrue();

    List<Place> findByHasElevatorTrue();

    List<Place> findByHasAccessibleToiletTrue();

    List<Place> findByHasWheelchairAccessTrue();

    List<Place> findByAccessibilityScoreGreaterThanEqualOrderByAccessibilityScoreDesc(Integer minScore);

    List<Place> findAllByOrderByAccessibilityScoreDesc();

    long countByAccessibilityStatus(AccessibilityStatus status);

    List<Place> findByAccessibilityStatus(AccessibilityStatus status);
}
