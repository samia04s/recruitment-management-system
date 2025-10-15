package com.example.recruitment.dto;

import jakarta.validation.constraints.NotNull;

public class ApplyJobRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    public Long getJobId() { return jobId; }

}
