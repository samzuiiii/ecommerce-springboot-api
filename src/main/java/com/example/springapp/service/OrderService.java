package com.example.springapp.service;

import com.example.springapp.model.Order;
import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    List<Order> getOrdersByUser(Long userId);
    Order getOrderById(Long orderId);
    Order updateOrderStatus(Long orderId, String status);
}