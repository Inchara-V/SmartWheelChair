package com.accessiblity.backend.repository;

import com.accessiblity.backend.entity.Report;
import com.accessiblity.backend.entity.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPlaceId(Long placeId);

    List<Report> findByStatus(ReportStatus status);

    List<Report> findByPlaceIdAndStatus(Long placeId, ReportStatus status);
}
