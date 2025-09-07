package com.essence_evoke.service;

import com.essence_evoke.dto.PurchaseHistoryDTO;
import com.essence_evoke.model.PurchaseHistory;
import com.essence_evoke.model.User;

import java.util.List;

public interface PurchaseHistoryService {
    PurchaseHistory save(PurchaseHistory purchaseHistory);
    List<PurchaseHistory> getByUser(User user);
    List<PurchaseHistoryDTO> getAllHistories();
    List<PurchaseHistoryDTO> getHistoriesByUser(User user);
    PurchaseHistory getPurchaseHistoryById(Long id);
}
