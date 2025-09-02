package com.essence_evoke.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter  // generates all getters
@Setter  // generates all setters/
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public VerificationToken() {
    }

    public VerificationToken(String token, User user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
