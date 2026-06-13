package com.accessiblity.backend.service;

import com.accessiblity.backend.dto.ReportDTO;
import com.accessiblity.backend.entity.Report;
import com.accessiblity.backend.entity.ReportStatus;
import com.accessiblity.backend.entity.Place;
import com.accessiblity.backend.entity.AccessibilityStatus;
import com.accessiblity.backend.exception.ResourceNotFoundException;
import com.accessiblity.backend.exception.ValidationException;
import com.accessiblity.backend.repository.ReportRepository;
import com.accessiblity.backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PlaceRepository placeRepository;
    private final AccessibilityScoreService scoreService;

    /**
     * Get all reports.
     *
     * @return list of all reports
     */
    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get reports for a specific place.
     *
     * @param placeId the place ID
     * @return list of reports for the place
     */
    public List<ReportDTO> getReportsByPlaceId(Long placeId) {
        return reportRepository.findByPlaceId(placeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get report by ID.
     *
     * @param id the report ID
     * @return report details
     * @throws ResourceNotFoundException if report not found
     */
    public ReportDTO getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        return convertToDTO(report);
    }

    /**
     * Create a new report for a place.
     *
     * @param reportDTO the report data
     * @return created report details
     * @throws ValidationException if report data is invalid
     */
    public ReportDTO createReport(ReportDTO reportDTO) {
        validateReportDTO(reportDTO);

        Report report = convertToEntity(reportDTO);
        Report savedReport = reportRepository.save(report);
        return convertToDTO(savedReport);
    }

    /**
     * Update an existing report.
     *
     * @param id        the report ID to update
     * @param reportDTO the updated report data
     * @return updated report details
     * @throws ResourceNotFoundException if report not found
     * @throws ValidationException      if report data is invalid
     */
    public ReportDTO updateReport(Long id, ReportDTO reportDTO) {
        validateReportDTO(reportDTO);

        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        // Update report fields
        report.setPlaceId(reportDTO.getPlaceId());
        report.setReporterName(reportDTO.getReporterName());
        report.setReporterEmail(reportDTO.getReporterEmail());
        report.setDescription(reportDTO.getDescription());
        report.setHasRamp(reportDTO.getHasRamp());
        report.setHasElevator(reportDTO.getHasElevator());
        report.setHasAccessibleToilet(reportDTO.getHasAccessibleToilet());
        report.setHasWheelchairAccess(reportDTO.getHasWheelchairAccess());
        report.setHasAccessibleParking(reportDTO.getHasAccessibleParking());
        report.setStatus(reportDTO.getStatus());

        Report updatedReport = reportRepository.save(report);
        return convertToDTO(updatedReport);
    }

    /**
     * Delete a report by ID.
     *
     * @param id the report ID
     * @throws ResourceNotFoundException if report not found
     */
    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }

    /**
     * Approve a report and update the place with approved data.
     * Triggers place score and status recalculation using the latest scoring formula.
     *
     * @param reportId the report ID to approve
     * @return the approved report
     * @throws ResourceNotFoundException if report not found
     * @throws ValidationException if report cannot be approved
     */
    public ReportDTO approveReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        // Check if report can be approved (must be in PENDING or REVIEWED status)
        if (report.getStatus() != ReportStatus.PENDING && report.getStatus() != ReportStatus.REVIEWED) {
            throw new ValidationException("Only PENDING or REVIEWED reports can be approved");
        }

        // Get the place to update
        Place place = placeRepository.findById(report.getPlaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with id: " + report.getPlaceId()));

        // Update place fields from approved report
        if (report.getHasRamp() != null) {
            place.setHasRamp(report.getHasRamp());
        }
        if (report.getHasElevator() != null) {
            place.setHasElevator(report.getHasElevator());
        }
        if (report.getHasAccessibleToilet() != null) {
            place.setHasAccessibleToilet(report.getHasAccessibleToilet());
        }
        if (report.getHasWheelchairAccess() != null) {
            place.setHasWheelchairAccess(report.getHasWheelchairAccess());
        }
        if (report.getHasAccessibleParking() != null) {
            place.setHasAccessibleParking(report.getHasAccessibleParking());
        }

        // Recalculate score with new formula
        int newScore = scoreService.calculateScore(place);
        place.setAccessibilityScore(newScore);

        // Recalculate status
        AccessibilityStatus newStatus = scoreService.calculateAccessibilityStatus(newScore);
        place.setAccessibilityStatus(newStatus);

        // Save updated place
        placeRepository.save(place);

        // Mark report as APPROVED
        report.setStatus(ReportStatus.APPROVED);
        reportRepository.save(report);

        return convertToDTO(report);
    }

    /**
     * Validate report DTO.
     *
     * @param reportDTO the report to validate
     * @throws ValidationException if validation fails
     */
    private void validateReportDTO(ReportDTO reportDTO) {
        if (reportDTO.getPlaceId() == null || reportDTO.getPlaceId() <= 0) {
            throw new ValidationException("Valid place ID is required");
        }
        if (reportDTO.getReporterName() == null || reportDTO.getReporterName().trim().isEmpty()) {
            throw new ValidationException("Reporter name is required");
        }
        if (reportDTO.getReporterEmail() == null || reportDTO.getReporterEmail().trim().isEmpty()) {
            throw new ValidationException("Reporter email is required");
        }
        if (reportDTO.getDescription() == null || reportDTO.getDescription().trim().isEmpty()) {
            throw new ValidationException("Description is required");
        }
    }

    /**
     * Convert Report entity to ReportDTO.
     *
     * @param report the report entity
     * @return the DTO
     */
    private ReportDTO convertToDTO(Report report) {
        return ReportDTO.builder()
                .id(report.getId())
                .placeId(report.getPlaceId())
                .reporterName(report.getReporterName())
                .reporterEmail(report.getReporterEmail())
                .description(report.getDescription())
                .hasRamp(report.getHasRamp())
                .hasElevator(report.getHasElevator())
                .hasAccessibleToilet(report.getHasAccessibleToilet())
                .hasWheelchairAccess(report.getHasWheelchairAccess())
                .hasAccessibleParking(report.getHasAccessibleParking())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }

    /**
     * Convert ReportDTO to Report entity.
     *
     * @param reportDTO the DTO
     * @return the entity
     */
    private Report convertToEntity(ReportDTO reportDTO) {
        return Report.builder()
                .placeId(reportDTO.getPlaceId())
                .reporterName(reportDTO.getReporterName())
                .reporterEmail(reportDTO.getReporterEmail())
                .description(reportDTO.getDescription())
                .hasRamp(reportDTO.getHasRamp())
                .hasElevator(reportDTO.getHasElevator())
                .hasAccessibleToilet(reportDTO.getHasAccessibleToilet())
                .hasWheelchairAccess(reportDTO.getHasWheelchairAccess())
                .hasAccessibleParking(reportDTO.getHasAccessibleParking())
                .status(reportDTO.getStatus() != null ? reportDTO.getStatus() : ReportStatus.PENDING)
                .build();
    }
}
