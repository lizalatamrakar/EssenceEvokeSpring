package com.essence_evoke.repository;

import com.essence_evoke.model.User;
import com.essence_evoke.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findByUser(User user);
}
