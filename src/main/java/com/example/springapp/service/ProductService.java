package com.example.springapp.service;

import com.example.springapp.model.Product;
import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product updateProduct(Long id, Product product);
    boolean deleteProduct(Long id);
    List<Product> searchByName(String name);
    List<Product> getProductsByCategory(String categoryName);
    List<Product> getRecommendations(Long productId);
}