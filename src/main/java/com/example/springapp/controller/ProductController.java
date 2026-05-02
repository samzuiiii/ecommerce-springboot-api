package com.example.springapp.controller;

import com.example.springapp.model.Product;
import com.example.springapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product saved = productService.addProduct(product);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list = productService.getAllProducts();
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
            @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        if (updated == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchByName(@RequestParam String name) {
        List<Product> list = productService.searchByName(name);
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Product>> getByCategory(
            @PathVariable String categoryName) {
        List<Product> list = productService.getProductsByCategory(categoryName);
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<Product>> getRecommendations(@PathVariable Long id) {
        List<Product> list = productService.getRecommendations(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}