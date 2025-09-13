package com.essence_evoke.model;

import jakarta.persistence.Entity;

import jakarta.persistence.*;


import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "checkout_items")
@Getter  // generates all getters
@Setter  // generates all setters/
public class CheckoutItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @org.hibernate.annotations.NotFound(action = org.hibernate.annotations.NotFoundAction.IGNORE)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price; // store as integer in smallest unit (e.g., paisa)

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE / INACTIVE
}

