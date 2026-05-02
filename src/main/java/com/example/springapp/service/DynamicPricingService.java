package com.example.springapp.service;

import com.example.springapp.model.Product;
import com.example.springapp.repository.OrderItemRepo;
import com.example.springapp.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class DynamicPricingService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    public Map<String, Object> getSuggestedPrice(Long productId) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Product not found");
            return error;
        }

        int stock = product.getStockQuantity();
        long orderCount = orderItemRepo.countByProductProductId(productId);
        double originalPrice = product.getPrice();
        double suggestedPrice = originalPrice;
        String reason;
        String trend;

        if (stock < 10 && orderCount > 5) {
            suggestedPrice = Math.round(originalPrice * 1.15 * 100.0) / 100.0;
            reason = "High demand with low stock";
            trend = "INCREASE";
        } else if (stock > 100 && orderCount < 3) {
            suggestedPrice = Math.round(originalPrice * 0.90 * 100.0) / 100.0;
            reason = "Low demand with high stock";
            trend = "DECREASE";
        } else if (stock < 20 && orderCount > 10) {
            suggestedPrice = Math.round(originalPrice * 1.20 * 100.0) / 100.0;
            reason = "Very high demand, critically low stock";
            trend = "INCREASE";
        } else {
            reason = "Price is optimal";
            trend = "STABLE";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("productName", product.getProductName());
        result.put("originalPrice", originalPrice);
        result.put("suggestedPrice", suggestedPrice);
        result.put("currentStock", stock);
        result.put("totalOrderCount", orderCount);
        result.put("trend", trend);
        result.put("reason", reason);
        return result;
    }
}