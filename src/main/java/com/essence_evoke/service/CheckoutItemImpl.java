package com.essence_evoke.service;

import com.essence_evoke.model.CheckoutItem;
import com.essence_evoke.model.Product;
import com.essence_evoke.model.User;
import com.essence_evoke.repository.CheckoutItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CheckoutItemImpl implements CheckoutItemService {
    private final CheckoutItemRepository checkoutItemRepository;

    @Autowired
    public CheckoutItemImpl(CheckoutItemRepository checkoutItemRepository) {
        this.checkoutItemRepository = checkoutItemRepository;
    }


    @Override
    //Add method for creating checkout items
    public void saveAll(List<CheckoutItem> checkoutItems) {
        checkoutItemRepository.saveAll(checkoutItems);
    }

    @Override
    //Add method for marking old checkout items inactive
    public void changeOldItemsStatus(User user, String status) {
        List<CheckoutItem> activeItems = checkoutItemRepository.findByUserAndStatus(user, "ACTIVE");
        for (CheckoutItem item : activeItems) {
            item.setStatus(status);
        }
        checkoutItemRepository.saveAll(activeItems);
    }

    @Override
    public List<CheckoutItem> findByUserAndStatus(User user, String status) {
        return checkoutItemRepository.findByUserAndStatus(user, status);
    }

    @Override
    public Optional<CheckoutItem> findById(Long id) {
        return checkoutItemRepository.findById(id);
    }

    @Override
    public void delete(CheckoutItem item) {
        checkoutItemRepository.delete(item);
    }

    @Override
    public List<CheckoutItem> findByProduct(Product product) {
        return checkoutItemRepository.findByProduct(product);
    }

    @Override
    public void deleteAll(List<CheckoutItem> checkoutItems) {
        checkoutItemRepository.deleteAll(checkoutItems);
    }

}