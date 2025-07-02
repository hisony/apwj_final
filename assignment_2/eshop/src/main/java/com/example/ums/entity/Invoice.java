package com.example.ums.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;




import java.time.LocalDateTime;
import java.util.List;

public class Invoice {

    private Long id;
    private Order order;
    private LocalDateTime invoiceDate = LocalDateTime.now();
    private double totalAmount;
    private double taxAmount;
    private double discountAmount;
    private double finalAmount;
    private List<InvoiceItem> items;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    // Methods to calculate amounts
    public void calculateTotalAmount() {
        totalAmount = items.stream().mapToDouble(item -> item.getUnitPrice() * item.getQuantity()).sum();
    }

    public void calculateTax() {
        taxAmount = totalAmount * 0.10;  // Assuming 10% tax
    }

    public void calculateDiscount() {
        discountAmount = items.stream()
                .filter(item -> item.getDiscount() > 0)
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity() * item.getDiscount() / 100)
                .sum();
    }

    public void calculateFinalAmount() {
        finalAmount = totalAmount + taxAmount - discountAmount;
    }
}
