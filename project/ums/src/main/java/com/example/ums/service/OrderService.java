package com.example.ums.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Place a new order
    public Order placeOrder(Order order) {
        orderRepository.save(order); // Save the order in the database
        return order;
    }

    // Get all orders for a specific user
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId); // Get all orders for a specific user
    }

    // Get all orders (admin functionality)
    public List<Order> getAllOrders() {
        return orderRepository.findAll(); // Get all orders from the database
    }
}
