package com.example.recruitment.controller;

import com.example.recruitment.dto.ApplyJobRequest;
import com.example.recruitment.model.Application;
import com.example.recruitment.model.Job;
import com.example.recruitment.model.User;
import com.example.recruitment.service.ApplicationService;
import com.example.recruitment.service.JobService;
import com.example.recruitment.service.ResumeService;
import com.example.recruitment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applicant")
public class ApplicantController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final ResumeService resumeService;
    private final UserService userService;

    public ApplicantController(JobService jobService,
                               ApplicationService applicationService,
                               ResumeService resumeService,
                               UserService userService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.resumeService = resumeService;
        this.userService = userService;
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
    
@PostMapping("/uploadResume")
public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file) {
    String email = getLoggedInUserEmail();
    if (email == null) {
        return ResponseEntity.status(401).body("Unauthorized");
    }
    Optional<User> userOpt = userService.findByEmail(email);
    if (userOpt.isEmpty()) {
        return ResponseEntity.status(404).body("User not found");
    }
    try {
        return ResponseEntity.ok(resumeService.uploadResume(userOpt.get().getId(), file));
    } catch (IOException e) {
        e.printStackTrace();  // Logs full stack trace on server console
        return ResponseEntity.badRequest().body("Failed to upload resume: " + e.getMessage());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Invalid file: " + e.getMessage());
    }
}


    @PostMapping("/apply")
    public ResponseEntity<?> applyToJob(@Valid @RequestBody ApplyJobRequest applyJobRequest) {
        String email = getLoggedInUserEmail();
        if (email == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        User user = userOpt.get();

        Optional<Job> jobOpt = jobService.getJobById(applyJobRequest.getJobId());
        if (jobOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Job not found");
        }

        Application application = Application.builder()
                .applicant(user)
                .job(jobOpt.get())
                .status("Applied")
                .build();

        return ResponseEntity.ok(applicationService.applyToJob(application));
    }
}
