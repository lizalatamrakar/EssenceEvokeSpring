package com.essence_evoke.service;

import com.essence_evoke.model.User;
import com.essence_evoke.model.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfile> findByUser(User user);
    // Add this method
    UserProfile save(UserProfile profile);
}
