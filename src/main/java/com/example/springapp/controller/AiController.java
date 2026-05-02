package com.example.springapp.controller;

import com.example.springapp.model.Product;
import com.example.springapp.service.DynamicPricingService;
import com.example.springapp.service.GeminiService;
import com.example.springapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DynamicPricingService dynamicPricingService;

    @PostMapping("/products/{id}/describe")
    public ResponseEntity<String> generateDescription(@PathVariable Long id) {
        String description = geminiService.generateDescription(id);
        return new ResponseEntity<>(description, HttpStatus.OK);
    }

    @GetMapping("/products/{id}/recommendations")
    public ResponseEntity<List<Product>> getRecommendations(@PathVariable Long id) {
        List<Product> recommendations = productService.getRecommendations(id);
        if (recommendations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

    @GetMapping("/products/{id}/pricing")
    public ResponseEntity<Map<String, Object>> getSuggestedPrice(
            @PathVariable Long id) {
        Map<String, Object> pricing = dynamicPricingService.getSuggestedPrice(id);
        return new ResponseEntity<>(pricing, HttpStatus.OK);
    }
}