package com.accessiblity.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "places")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlaceType placeType;

    @Column(nullable = false)
    private Boolean hasRamp = false;

    @Column(nullable = false)
    private Boolean hasElevator = false;

    @Column(nullable = false)
    private Boolean hasAccessibleToilet = false;

    @Column(nullable = false)
    private Boolean hasWheelchairAccess = false;

    @Column(name = "has_accessible_parking")
    private Boolean hasAccessibleParking = false;

    @Column(name = "accessibility_score")
    private Integer accessibilityScore;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "accessibility_status")
    private AccessibilityStatus accessibilityStatus;

    @PrePersist
    protected void onCreate() {
        createdAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis();
        updateAccessibilityStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = System.currentTimeMillis();
        updateAccessibilityStatus();
    }

    /**
     * Calculate accessibility status based on score.
     * Score >= 80: ACCESSIBLE (Green)
     * Score 50-79: PARTIAL (Yellow)
     * Score < 50: NOT_ACCESSIBLE (Red)
     */
    private void updateAccessibilityStatus() {
        if (this.accessibilityScore == null || this.accessibilityScore < 50) {
            this.accessibilityStatus = AccessibilityStatus.NOT_ACCESSIBLE;
        } else if (this.accessibilityScore < 80) {
            this.accessibilityStatus = AccessibilityStatus.PARTIAL;
        } else {
            this.accessibilityStatus = AccessibilityStatus.ACCESSIBLE;
        }
    }
}
