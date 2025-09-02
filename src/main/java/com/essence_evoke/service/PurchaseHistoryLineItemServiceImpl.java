package com.essence_evoke.service;

import com.essence_evoke.model.PurchaseHistoryLineItem;
import com.essence_evoke.repository.PurchaseHistoryLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseHistoryLineItemServiceImpl implements PurchaseHistoryLineItemService {
    @Autowired
    private PurchaseHistoryLineItemRepository lineItemRepository;

    @Override
    public PurchaseHistoryLineItem save(PurchaseHistoryLineItem item) {
        return lineItemRepository.save(item);
    }
}
