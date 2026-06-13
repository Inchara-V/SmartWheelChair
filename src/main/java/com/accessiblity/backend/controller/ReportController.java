package com.accessiblity.backend.controller;

import com.accessiblity.backend.dto.ReportDTO;
import com.accessiblity.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Get all reports.
     *
     * @return list of all reports
     */
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Get reports for a specific place.
     *
     * @param placeId the place ID
     * @return list of reports for the place
     */
    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<ReportDTO>> getReportsByPlaceId(@PathVariable Long placeId) {
        List<ReportDTO> reports = reportService.getReportsByPlaceId(placeId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Get a report by ID.
     *
     * @param id the report ID
     * @return the report details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        ReportDTO report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    /**
     * Create a new report.
     *
     * @param reportDTO the report data
     * @return the created report
     */
    @PostMapping
    public ResponseEntity<ReportDTO> createReport(@RequestBody ReportDTO reportDTO) {
        ReportDTO createdReport = reportService.createReport(reportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    /**
     * Update an existing report.
     *
     * @param id        the report ID
     * @param reportDTO the updated report data
     * @return the updated report
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateReport(@PathVariable Long id, @RequestBody ReportDTO reportDTO) {
        ReportDTO updatedReport = reportService.updateReport(id, reportDTO);
        return ResponseEntity.ok(updatedReport);
    }

    /**
     * Delete a report.
     *
     * @param id the report ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
