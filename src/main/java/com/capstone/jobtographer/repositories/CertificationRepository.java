package com.capstone.jobtographer.repositories;

import com.capstone.jobtographer.models.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Certification findCertificationByCertificationName(String certName);
}
