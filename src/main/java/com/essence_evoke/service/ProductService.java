package com.essence_evoke.service;

import com.essence_evoke.model.Product;

import java.util.List;

public interface ProductService {
	List<Product> getAllProducts();
	void saveProduct(Product product);
	Product getProductById(Long id);
	void deleteProductById(Long id);
	List<Product> getTopPickProducts();
	List<Product> searchProducts(String keyword);
}
