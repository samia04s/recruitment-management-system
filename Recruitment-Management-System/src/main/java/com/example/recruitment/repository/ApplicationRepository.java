package com.example.recruitment.repository;

import com.example.recruitment.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByApplicantId(Long applicantId);
    List<Application> findByJobId(Long jobId);
}
