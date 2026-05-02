package com.example.springapp.service;

import com.example.springapp.model.Product;
import com.example.springapp.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existing = productRepo.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setProductName(product.getProductName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setCategory(product.getCategory());
        return productRepo.save(existing);
    }

    @Override
    public boolean deleteProduct(Long id) {
        Product existing = productRepo.findById(id).orElse(null);
        if (existing == null) return false;
        productRepo.deleteById(id);
        return true;
    }

    @Override
    public List<Product> searchByName(String name) {
        return productRepo.findByProductNameContaining(name);
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepo.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getRecommendations(Long productId) {
        return productRepo.findRecommendations(
            productId,
            PageRequest.of(0, 5)
        );
    }
}