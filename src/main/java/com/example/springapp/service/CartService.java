package com.example.springapp.service;

import com.example.springapp.model.Cart;

public interface CartService {
    Cart getCartByUserId(Long userId);
    Cart addToCart(Long userId, Long productId, int quantity);
    Cart updateCartItem(Long userId, Long cartItemId, int quantity);
    Cart removeFromCart(Long userId, Long cartItemId);
    Cart clearCart(Long userId);
}
