package com.example.springapp.service;

import com.example.springapp.model.*;
import com.example.springapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderRepo orderRepo;
    @Autowired private UserRepo userRepo;
    @Autowired private CartRepo cartRepo;
    @Autowired private ProductRepo productRepo;

    @Override
    @Transactional
    public Order placeOrder(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found"));
        Cart cart = cartRepo.findByUser(user);

        if (cart == null || cart.getItems().isEmpty())
            throw new RuntimeException("Cart is empty");

        for (CartItem ci : cart.getItems()) {
            if (ci.getProduct().getStockQuantity() < ci.getQuantity())
                throw new RuntimeException(
                    "Insufficient stock for: " + ci.getProduct().getProductName());
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        double total = 0;

        for (CartItem ci : cart.getItems()) {
            Product p = ci.getProduct();
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(p);
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtOrder(p.getPrice());
            order.getItems().add(oi);
            p.setStockQuantity(p.getStockQuantity() - ci.getQuantity());
            productRepo.save(p);
            total += p.getPrice() * ci.getQuantity();
        }

        order.setTotalAmount(total);
        orderRepo.save(order);
        cart.getItems().clear();
        return order;
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return null;
        return orderRepo.findByUser(user);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null) return null;
        order.setStatus(status);
        return orderRepo.save(order);
    }
}
