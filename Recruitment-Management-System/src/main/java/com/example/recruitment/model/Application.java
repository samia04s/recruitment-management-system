package com.example.recruitment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="applicant_id")
    private User applicant;

    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;

    private String status; // e.g. Applied, Rejected, Selected
}
