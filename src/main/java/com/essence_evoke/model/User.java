package com.essence_evoke.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Getter  // generates all getters
@Setter  // generates all setters/
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    private String role = "ROLE_USER"; // default role

    @Column(nullable = false)
    private boolean enabled = false;

    @ManyToMany
    @JoinTable(
            name = "user_wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )

    private Set<Product> wishlist = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_cart",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )

    private Set<Product> cart = new HashSet<>();

}

