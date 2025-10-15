package com.example.recruitment.service;

import com.example.recruitment.model.Job;
import com.example.recruitment.repository.JobRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(Job job) {
        job.setPostedOn(LocalDateTime.now());
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAllByOrderByPostedOnDesc();
    }
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

}
