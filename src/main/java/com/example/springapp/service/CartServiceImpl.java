package com.example.springapp.service;

import com.example.springapp.model.*;
import com.example.springapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartRepo cartRepo;
    @Autowired private UserRepo userRepo;
    @Autowired private ProductRepo productRepo;
    @Autowired private CartItemRepo cartItemRepo;

    @Override
    public Cart getCartByUserId(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return null;
        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepo.save(cart);
        }
        return cart;
    }

    @Override
    public Cart addToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) return null;

        Optional<CartItem> existing = cart.getItems().stream()
            .filter(i -> i.getProduct().getProductId().equals(productId))
            .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            cart.getItems().add(item);
        }
        return cartRepo.save(cart);
    }

    @Override
    public Cart updateCartItem(Long userId, Long cartItemId, int quantity) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().stream()
            .filter(i -> i.getCartItemId().equals(cartItemId))
            .findFirst()
            .ifPresent(i -> i.setQuantity(quantity));
        return cartRepo.save(cart);
    }

    @Override
    public Cart removeFromCart(Long userId, Long cartItemId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(i -> i.getCartItemId().equals(cartItemId));
        return cartRepo.save(cart);
    }

    @Override
    public Cart clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        return cartRepo.save(cart);
    }
}