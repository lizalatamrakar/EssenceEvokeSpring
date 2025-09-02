package com.essence_evoke.repository;

import com.essence_evoke.model.CheckoutItem;
import com.essence_evoke.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CheckoutItemRepository extends JpaRepository<CheckoutItem,Long> {
    Optional<CheckoutItem> findById(Long id);
    List<CheckoutItem> findByUserAndStatus(User user, String status);
    void delete(CheckoutItem item);
}
