package com.essence_evoke.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private LocalDate dateOfBirth;

    private String gender; // You can use enum for better safety

    private String profilePicture; // Store as file path or URL

    // One-to-one mapping with User
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
