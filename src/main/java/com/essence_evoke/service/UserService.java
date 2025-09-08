package com.essence_evoke.service;

import com.essence_evoke.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User registerNewUser(User user);
    User updateUser(User user);
    void createVerificationToken(User user, String token);
    List<User> getAllUsers();
    User save(User user);
}
