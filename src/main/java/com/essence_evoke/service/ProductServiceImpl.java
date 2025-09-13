package com.essence_evoke.service;

import java.util.List;
import java.util.Optional;

import com.essence_evoke.model.CheckoutItem;
import com.essence_evoke.repository.CheckoutItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.essence_evoke.model.Product;
import com.essence_evoke.repository.ProductRepository;

@Service                   // ← this makes Spring pick it up as a bean
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CheckoutItemService checkoutItemService;

    @Autowired             // constructor injection
    public ProductServiceImpl(ProductRepository productRepository, CheckoutItemService checkoutItemService) {
        this.productRepository = productRepository;
        this.checkoutItemService = checkoutItemService;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    @Override
    public void deleteProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Delete all checkout items linked to this product
        List<CheckoutItem> checkoutItems = checkoutItemService.findByProduct(product);
        checkoutItemService.deleteAll(checkoutItems);

        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getTopPickProducts() {
        return productRepository.findTop4ByIsTopPickTrue();
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

}
