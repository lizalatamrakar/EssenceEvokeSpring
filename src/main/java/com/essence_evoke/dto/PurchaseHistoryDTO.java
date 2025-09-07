package com.essence_evoke.dto;

import java.util.List;

public record PurchaseHistoryDTO(
        Long id,
        String transactionId,
        float totalAmount,
        String status,
        String paymentMethod,
        String purchaseDate,
        String userEmail,
        List<PurchaseHistoryLineItemDTO> lineItems
) {
}
