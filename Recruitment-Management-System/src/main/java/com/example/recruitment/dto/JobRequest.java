package com.example.recruitment.dto;

import jakarta.validation.constraints.NotBlank;

public class JobRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    private String companyName;

    // Getters and setters
}
