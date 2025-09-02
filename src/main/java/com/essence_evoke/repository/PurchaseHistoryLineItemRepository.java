package com.essence_evoke.repository;

import com.essence_evoke.model.PurchaseHistoryLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseHistoryLineItemRepository extends JpaRepository<PurchaseHistoryLineItem,Long> {
}
