package com.accessiblity.backend.dto;

import com.accessiblity.backend.entity.ReportStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long id;
    
    @NotNull(message = "Place ID is required")
    private Long placeId;
    
    @NotBlank(message = "Reporter name is required")
    private String reporterName;
    
    @NotBlank(message = "Reporter email is required")
    @Email(message = "Valid email is required")
    private String reporterEmail;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private Boolean hasRamp;
    private Boolean hasElevator;
    private Boolean hasAccessibleToilet;
    private Boolean hasWheelchairAccess;
    private Boolean hasAccessibleParking;
    
    private ReportStatus status;
    private Long createdAt;
    private Long updatedAt;
}
