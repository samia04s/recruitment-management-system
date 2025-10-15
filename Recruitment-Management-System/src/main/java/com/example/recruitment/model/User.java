package com.example.recruitment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String userType; // "Admin" or "Applicant"

    private String profileHeadline;

    private String address;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;
}
