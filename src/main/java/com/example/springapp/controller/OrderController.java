package com.example.springapp.controller;

import com.example.springapp.model.Order;
import com.example.springapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}/place")
    public ResponseEntity<?> placeOrder(@PathVariable Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        if (orders == null || orders.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/detail/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long orderId,
            @RequestParam String status) {
        Order order = orderService.updateOrderStatus(orderId, status);
        if (order == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}