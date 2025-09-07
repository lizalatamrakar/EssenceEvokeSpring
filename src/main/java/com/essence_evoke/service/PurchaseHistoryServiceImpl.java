package com.essence_evoke.service;

import com.essence_evoke.dto.PurchaseHistoryDTO;
import com.essence_evoke.dto.PurchaseHistoryLineItemDTO;
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

    // Convert Entity -> DTO
    private PurchaseHistoryDTO toDTO(PurchaseHistory ph) {
        return new PurchaseHistoryDTO(
                ph.getId(),
                ph.getTransactionId(),
                ph.getTotalAmount(),
                ph.getStatus(),
                ph.getPaymentMethod(),
                ph.getPurchaseDate().toString(),
                ph.getUser().getEmail(),
                ph.getLineItems().stream().map(li ->
                        new PurchaseHistoryLineItemDTO(
                                li.getProduct().getName(),
                                li.getQuantity(),
                                li.getPriceAtPurchase()
                        )
                ).toList()
        );
    }

    @Override
    public List<PurchaseHistoryDTO> getAllHistories() {
        return purchaseHistoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<PurchaseHistoryDTO> getHistoriesByUser(User user) {
        return purchaseHistoryRepository.findByUser(user)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public PurchaseHistory getPurchaseHistoryById(Long id) {
        return purchaseHistoryRepository.findById(id).orElse(null);
    }
}
