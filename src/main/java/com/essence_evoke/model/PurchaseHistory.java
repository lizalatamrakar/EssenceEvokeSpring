package com.essence_evoke.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "purchase_history")
@Getter  // generates all getters
@Setter  // generates all setters/
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private float totalAmount; // in paisa/cents

    @Column(nullable = false)
    private String status = "COMPLETED"; // COMPLETED, CANCELLED, PENDING

    @Column(nullable = false, unique = true)
    private String transactionId; // reference to payment processor

    @Column(nullable = false)
    private LocalDateTime purchaseDate = LocalDateTime.now();

    @Column(nullable = false)
    private String paymentMethod;

    @OneToMany(mappedBy = "purchaseHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PurchaseHistoryLineItem> lineItems = new HashSet<>();
}

