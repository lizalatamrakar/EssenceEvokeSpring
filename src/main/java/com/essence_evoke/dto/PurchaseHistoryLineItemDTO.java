package com.essence_evoke.dto;

public record PurchaseHistoryLineItemDTO(
        String productName,
        int quantity,
        float priceAtPurchase
) {
}
