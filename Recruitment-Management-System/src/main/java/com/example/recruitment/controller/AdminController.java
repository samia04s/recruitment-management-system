package com.example.recruitment.controller;

import com.example.recruitment.model.Job;
import com.example.recruitment.model.User;
import com.example.recruitment.service.JobService;
import com.example.recruitment.service.UserService;
import com.example.recruitment.model.Application;
import com.example.recruitment.model.Profile;
import com.example.recruitment.service.ApplicationService;
import com.example.recruitment.repository.ProfileRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final JobService jobService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final ProfileRepository profileRepository;


    public AdminController(JobService jobService, UserService userService,
                           ApplicationService applicationService, ProfileRepository profileRepository) {
        this.jobService = jobService;
        this.userService = userService;
        this.applicationService = applicationService;
        this.profileRepository = profileRepository;
    }


    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    @PostMapping("/job")
    public ResponseEntity<?> createJob(@RequestBody Job job) {
        String email = getLoggedInUserEmail();
        if (email == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        Optional<User> adminOpt = userService.findByEmail(email);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Admin user not found");
        }

        User admin = adminOpt.get();
        job.setPostedBy(admin);
        Job created = jobService.createJob(job);

        return ResponseEntity.ok(created);
    }
    @GetMapping("/job/{jobId}/applicants")
    public ResponseEntity<?> getApplicantsForJob(@PathVariable Long jobId) {
        Optional<Job> jobOpt = jobService.getJobById(jobId);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Job not found");
        }
        List<Application> applications = applicationService.getApplicationsByJob(jobId);
        List<User> applicants = applications.stream()
                .map(Application::getApplicant)
                .toList();
        return ResponseEntity.ok(applicants);
    }

    @GetMapping("/applicants")
    public ResponseEntity<List<User>> getAllApplicants() {
        List<User> applicants = userService.findAllByUserType("Applicant");
        return ResponseEntity.ok(applicants);
    }

    @GetMapping("/applicant/{applicantId}/profile")
    public ResponseEntity<?> getApplicantProfile(@PathVariable Long applicantId) {
        Profile profile = profileRepository.findByUserId(applicantId);
        if (profile == null) {
            return ResponseEntity.badRequest().body("Applicant profile not found");
        }
        return ResponseEntity.ok(profile);
    }



}
