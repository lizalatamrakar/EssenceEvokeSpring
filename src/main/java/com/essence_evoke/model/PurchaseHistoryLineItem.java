package com.essence_evoke.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "purchase_history_line_items")
@Getter  // generates all getters
@Setter  // generates all setters/
public class PurchaseHistoryLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "purchase_history_id", nullable = false)
    private PurchaseHistory purchaseHistory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private float priceAtPurchase; // snapshot price in paisa/cents
}

