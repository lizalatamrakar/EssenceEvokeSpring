package com.essence_evoke.repository;

import com.essence_evoke.model.User;
import com.essence_evoke.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
}
