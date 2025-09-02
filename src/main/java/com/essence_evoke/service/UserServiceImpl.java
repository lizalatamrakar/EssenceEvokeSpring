package com.essence_evoke.service;

import com.essence_evoke.model.User;
import com.essence_evoke.model.VerificationToken;
import com.essence_evoke.repository.UserRepository;
import com.essence_evoke.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            VerificationTokenRepository tokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // For registration
    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false); // always disabled until confirmed
        return userRepository.save(user);
    }

    // For enabling or updating user info
    public User updateUser(User user) {
        return userRepository.save(user); // no re-encoding
    }

    @Override
    public void createVerificationToken(User user, String token) {
        Optional<VerificationToken> optionalToken = tokenRepository.findByUser(user);

        VerificationToken verificationToken;
        if (optionalToken.isPresent()) {
            // Update the existing token
            verificationToken = optionalToken.get();
            verificationToken.setToken(token);
            verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        } else {
            // Create a new token
            verificationToken = new VerificationToken(
                    token,
                    user,
                    LocalDateTime.now().plusHours(24)
            );
        }

        tokenRepository.save(verificationToken); // safe save/update

    }
}
