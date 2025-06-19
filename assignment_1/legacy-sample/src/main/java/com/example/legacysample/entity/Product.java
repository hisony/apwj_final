package com.example.legacysample.entity;

import java.time.LocalDate;

public class Product {

    public enum Category {
        BEAUTY_CARE, VEGETABLES, MEAT, GROCERIES, OTHERS
    }

    private int id;
    private String name;
    private Category category;
    private double price;
    private int quantity;
    private LocalDate expiryDate;
    private Double discount;

    public Product() {}

    public Product(int id, String name, Category category, double price, int quantity, LocalDate expiryDate, Double discount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.discount = (discount != null) ? discount : 0.0;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public double getDiscountedPrice() {
        if (discount != null && discount > 0) {
            return price * (1 - discount / 100);
        }
        return price;
    }

    public boolean isExpiringSoon() {
        if (expiryDate != null) {
            LocalDate now = LocalDate.now();
            return !expiryDate.isBefore(now) && !expiryDate.isAfter(now.plusDays(7));
        }
        return false;
    }

    public boolean isExpired() {
        if (expiryDate != null) {
            return expiryDate.isBefore(LocalDate.now());
        }
        return false;
    }
}

