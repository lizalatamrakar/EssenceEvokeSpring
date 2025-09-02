package com.essence_evoke.service;

import com.essence_evoke.model.Product;
import com.essence_evoke.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service                   // ← this makes Spring pick it up as a bean
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired             // constructor injection
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getTopPickProducts() {
        return productRepository.findTop4ByIsTopPickTrue();
    }

}
