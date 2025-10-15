package com.example.recruitment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resumeFilePath;

    @Column(length=2000)
    private String skills;

    @Column(length=2000)
    private String education;

    @Column(length=2000)
    private String experience;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
}
