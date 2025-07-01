package com.example.ums.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, OrderRepository orderRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
    }

    public Invoice generateInvoice(Long orderId) {
        // Fetch order details
        Order order = orderRepository.findById(orderId);

        // Create invoice items from order items
        List<InvoiceItem> items = createInvoiceItems(order);

        // Create invoice and calculate amounts
        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setItems(items);
        invoice.calculateTotalAmount();
        invoice.calculateDiscount();
        invoice.calculateTax();
        invoice.calculateFinalAmount();

        // Save the invoice
        invoiceRepository.save(invoice);

        return invoice;
    }

    private List<InvoiceItem> createInvoiceItems(Order order) {
        List<InvoiceItem> items = new ArrayList<>();
        // Map order items to invoice items
        for (OrderItem orderItem : order.getOrderItems()) {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProductId(orderItem.getProduct().getId());
            invoiceItem.setProductName(orderItem.getProduct().getName());
            invoiceItem.setQuantity(orderItem.getQuantity());
            invoiceItem.setUnitPrice(orderItem.getProduct().getPrice());
            invoiceItem.setDiscount(orderItem.getProduct().getDiscount());
            items.add(invoiceItem);
        }
        return items;
    }
}

