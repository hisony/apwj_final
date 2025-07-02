package com.example.ums.api;


import com.example.ums.entity.Order;
import com.example.ums.entity.Invoice;
import com.example.ums.service.OrderService;
import com.example.ums.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderApi {

    private final OrderService orderService;
    private final InvoiceService invoiceService;

    @Autowired
    public OrderApi(OrderService orderService, InvoiceService invoiceService) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
    }

    // Endpoint to place a new order

    @PostMapping("/place")
    public Order placeOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }

    // Endpoint to view all orders for a user
    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

//    // Endpoint to get the invoice for a specific order
//    @GetMapping("/invoice/{orderId}")
//    public Invoice getInvoice(@PathVariable Long orderId) {
//        return invoiceService.getInvoiceByOrderId(orderId);
//    }

    // Endpoint to get all orders (Admin only)
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}

