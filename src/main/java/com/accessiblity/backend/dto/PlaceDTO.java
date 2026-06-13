package com.accessiblity.backend.dto;

import com.accessiblity.backend.entity.PlaceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDTO {
    private Long id;
    
    @NotBlank(message = "Place name is required")
    private String name;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @Min(value = -90, message = "Latitude must be between -90 and 90")
    @Max(value = 90, message = "Latitude must be between -90 and 90")
    private Double latitude;
    
    @Min(value = -180, message = "Longitude must be between -180 and 180")
    @Max(value = 180, message = "Longitude must be between -180 and 180")
    private Double longitude;
    
    private String description;
    private PlaceType placeType;
    private Boolean hasRamp;
    private Boolean hasElevator;
    private Boolean hasAccessibleToilet;
    private Boolean hasWheelchairAccess;
    private Boolean hasAccessibleParking;
    
    @Min(value = 0, message = "Score must be between 0 and 100")
    @Max(value = 100, message = "Score must be between 0 and 100")
    private Integer accessibilityScore;
    
    private String accessibilityStatus;
    private String imageUrl;
    private Long createdAt;
    private Long updatedAt;
}
