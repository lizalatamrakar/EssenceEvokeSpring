package com.essence_evoke.service;

import com.essence_evoke.model.User;
import com.essence_evoke.model.UserProfile;
import com.essence_evoke.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<UserProfile> findByUser(User user) {
        return userProfileRepository.findByUser(user);
    }

    @Override
    public UserProfile save(UserProfile profile) {
        return userProfileRepository.save(profile);
    }
}
