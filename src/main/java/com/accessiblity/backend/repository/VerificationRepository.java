package com.accessiblity.backend.repository;

import com.accessiblity.backend.entity.Verification;
import com.accessiblity.backend.entity.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    List<Verification> findByPlaceId(Long placeId);

    List<Verification> findByVerificationStatus(VerificationStatus verificationStatus);

    List<Verification> findByPlaceIdAndVerificationStatus(Long placeId, VerificationStatus verificationStatus);
}
