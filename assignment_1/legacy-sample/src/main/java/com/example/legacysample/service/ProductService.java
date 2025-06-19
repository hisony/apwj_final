package com.example.legacysample.service;

import com.example.legacysample.entity.Product;
import com.example.legacysample.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Constructor injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        applyDiscount(product);  // Apply discount automatically on creation
        productRepository.create(product);
        return product;
    }

    public void create2(List<Product> products) {
        productRepository.create2(products);
    }



    public List<Product> getAll() {
        return productRepository.getAll();
    }

    public Product get(int id) {
        return productRepository.get(id);
    }

    public boolean update(Product updatedProduct) {
        applyDiscount(updatedProduct);  // Apply discount before updating
        return productRepository.update(updatedProduct);
    }

    public boolean delete(int id) {
        return productRepository.delete(id);
    }

    public List<Product> getProductsByCategory(Product.Category category) {
        return productRepository.getProductsByCategory(category);
    }

    public List<Product> getExpiringInNextDays(int days) {
        return productRepository.getExpiringInNextDays(days);
    }

    public List<Product> getExpiredProducts() {
        return productRepository.getExpiredProducts();
    }

    public Map<Product.Category, Double> getTotalValueGroupedByCategory() {
        return productRepository.getTotalValueGroupedByCategory();
    }

    public Map<Product.Category, List<Product>> getDiscountedProductsByCategory() {
        return productRepository.getDiscountedProductsByCategory();
    }

    public List<Product> getExpiringInNext7Days() {
        List<Product> expiringProducts = productRepository.getExpiringInNextDays(7);
        expiringProducts.forEach(this::applyDiscount);
        return expiringProducts;
    }

    // Apply discount automatically based on expiry date
    private void applyDiscount(Product product) {
        int daysLeft = getDaysUntilExpiry(product);
        double discount = calculateDiscountBasedOnExpiry(daysLeft);
        product.setDiscount(discount); // Apply discount automatically
        double discountedPrice = product.getDiscountedPrice(); // Apply the discount to the price
        product.setPrice(discountedPrice); // Update the price
    }

    // Calculate the discount based on how many days are left until the product expires
    private double calculateDiscountBasedOnExpiry(int daysLeft) {
        switch (daysLeft) {
            case 1: return 35.0;
            case 2: return 30.0;
            case 3: return 25.0;
            case 4: return 20.0;
            case 5: return 15.0;
            case 6: return 10.0;
            case 7: return 5.0;
            default: return 0.0;
        }
    }

    // Get the number of days until the product expires
    private int getDaysUntilExpiry(Product product) {
        if (product.getExpiryDate() != null) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), product.getExpiryDate());
        }
        return Integer.MAX_VALUE;
    }
}


