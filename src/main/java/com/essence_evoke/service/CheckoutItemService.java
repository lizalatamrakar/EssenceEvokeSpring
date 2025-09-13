package com.essence_evoke.service;

import com.essence_evoke.model.CheckoutItem;
import com.essence_evoke.model.Product;
import com.essence_evoke.model.User;

import java.util.List;
import java.util.Optional;

public interface CheckoutItemService {
    void saveAll(List<CheckoutItem> checkoutItems);

    void changeOldItemsStatus(User user, String status);

    List<CheckoutItem> findByUserAndStatus(User user, String status);
    Optional<CheckoutItem> findById(Long id);
    void delete(CheckoutItem item);
    List<CheckoutItem> findByProduct(Product product);
    void deleteAll(List<CheckoutItem> checkoutItems);
}
