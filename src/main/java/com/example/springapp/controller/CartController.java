package com.example.springapp.controller;

import com.example.springapp.model.Cart;
import com.example.springapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        Cart cart = cartService.addToCart(userId, productId, quantity);
        if (cart == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/{userId}/update/{cartItemId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {
        Cart cart = cartService.updateCartItem(userId, cartItemId, quantity);
        if (cart == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/remove/{cartItemId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long userId,
            @PathVariable Long cartItemId) {
        Cart cart = cartService.removeFromCart(userId, cartItemId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Cart> clearCart(@PathVariable Long userId) {
        Cart cart = cartService.clearCart(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}