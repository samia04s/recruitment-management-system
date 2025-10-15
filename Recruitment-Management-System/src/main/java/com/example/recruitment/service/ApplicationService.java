package com.example.recruitment.service;

import com.example.recruitment.model.Application;
import com.example.recruitment.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application applyToJob(Application application) {
        application.setStatus("Applied");
        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsByApplicant(Long applicantId) {
        return applicationRepository.findByApplicantId(applicantId);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}
