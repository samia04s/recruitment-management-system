package com.example.recruitment.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(length=5000)
    private String description;

    @Column(nullable=false)
    private LocalDateTime postedOn;

    private String companyName;

    @ManyToOne
    @JoinColumn(name="posted_by")
    private User postedBy;
}
