package com.example.springapp.service;

import com.example.springapp.model.Product;
import com.example.springapp.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Autowired
    private ProductRepo productRepo;

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateDescription(Long productId) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) return "Product not found";

        String prompt = "Write a 2 sentence product description for: "
            + product.getProductName()
            + " in the category: "
            + product.getCategory().getCategoryName()
            + ". Be concise and professional.";

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
            + "gemini-2.0-flash-lite:generateContent?key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        try {
            Map response = restTemplate.postForObject(url, body, Map.class);
            List candidates = (List) response.get("candidates");
            Map candidate = (Map) candidates.get(0);
            Map contentMap = (Map) candidate.get("content");
            List parts = (List) contentMap.get("parts");
            Map firstPart = (Map) parts.get(0);
            String generatedText = (String) firstPart.get("text");

            product.setDescription(generatedText);
            productRepo.save(product);
            return generatedText;

        } catch (Exception e) {
            return "Could not generate description: " + e.getMessage();
        }
    }
}