package com.essence_evoke.service;

import com.essence_evoke.model.PurchaseHistory;
import com.essence_evoke.model.User;
import com.essence_evoke.repository.PurchaseHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;

    @Override
    public PurchaseHistory save(PurchaseHistory purchaseHistory) {
        return purchaseHistoryRepository.save(purchaseHistory);
    }

    @Override
    public List<PurchaseHistory> getByUser(User user) {
        return purchaseHistoryRepository.findByUser(user);
    }
}
