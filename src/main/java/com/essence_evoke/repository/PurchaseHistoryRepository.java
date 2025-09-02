package com.essence_evoke.repository;

import com.essence_evoke.model.PurchaseHistory;
import com.essence_evoke.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory,Long> {
    List<PurchaseHistory> findByUser(User user);
}
