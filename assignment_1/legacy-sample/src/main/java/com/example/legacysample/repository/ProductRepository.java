package com.example.legacysample.repository;

import com.example.legacysample.entity.Product;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private static final List<Product> productList = new ArrayList<>();

    static {
        // Sample Products
        productList.add(new Product(1, "Shampoo", Product.Category.BEAUTY_CARE, 250.0, 5, LocalDate.now().plusDays(5), 20.0));
        productList.add(new Product(2, "Tomatoes", Product.Category.VEGETABLES, 40.0, 15, LocalDate.now().plusDays(2), 0.0));
        productList.add(new Product(3, "Beef", Product.Category.MEAT, 700.0, 8, LocalDate.now().plusDays(10), 0.0));
        productList.add(new Product(4, "Rice", Product.Category.GROCERIES, 60.0, 20, null, 0.0));
        productList.add(new Product(5, "Cucumber", Product.Category.VEGETABLES, 35.0, 10, LocalDate.now().minusDays(1), 0.0));
        productList.add(new Product(6, "Face Wash", Product.Category.BEAUTY_CARE, 300.0, 4, LocalDate.now().plusDays(7), 15.0));
        productList.add(new Product(7, "Toothpaste", Product.Category.BEAUTY_CARE, 120.0, 30, LocalDate.now().plusDays(3), 10.0));
        productList.add(new Product(8, "Carrot", Product.Category.VEGETABLES, 45.0, 20, LocalDate.now().plusDays(4), 0.0));
        productList.add(new Product(9, "Chicken Breast", Product.Category.MEAT, 500.0, 10, LocalDate.now().plusDays(6), 5.0));
        productList.add(new Product(10, "Olive Oil", Product.Category.GROCERIES, 350.0, 7, LocalDate.now().plusDays(8), 20.0));
        productList.add(new Product(11, "Potatoes", Product.Category.VEGETABLES, 25.0, 50, LocalDate.now().plusDays(12), 0.0));
        productList.add(new Product(12, "Beef Steaks", Product.Category.MEAT, 800.0, 6, LocalDate.now().plusDays(15), 10.0));
        productList.add(new Product(13, "Rice Flour", Product.Category.GROCERIES, 55.0, 25, LocalDate.now().plusDays(10), 0.0));
        productList.add(new Product(14, "Toothbrush", Product.Category.BEAUTY_CARE, 50.0, 40, LocalDate.now().plusDays(2), 0.0));
        productList.add(new Product(15, "Lettuce", Product.Category.VEGETABLES, 30.0, 12, LocalDate.now().plusDays(5), 0.0));
        productList.add(new Product(16, "Yogurt", Product.Category.GROCERIES, 90.0, 10, LocalDate.now().plusDays(6), 0.0));
        productList.add(new Product(17, "Shaving Cream", Product.Category.BEAUTY_CARE, 200.0, 8, LocalDate.now().plusDays(4), 25.0));
        productList.add(new Product(18, "Pork Ribs", Product.Category.MEAT, 900.0, 5, LocalDate.now().plusDays(11), 0.0));
        productList.add(new Product(19, "Spinach", Product.Category.VEGETABLES, 40.0, 18, LocalDate.now().plusDays(3), 0.0));
        productList.add(new Product(20, "Canned Beans", Product.Category.GROCERIES, 60.0, 30, LocalDate.now().plusDays(14), 10.0));
        productList.add(new Product(21, "Face Cream", Product.Category.BEAUTY_CARE, 450.0, 3, LocalDate.now().plusDays(5), 30.0));
        productList.add(new Product(22, "Sweet Potatoes", Product.Category.VEGETABLES, 35.0, 15, LocalDate.now().plusDays(2), 0.0));
        productList.add(new Product(23, "Salmon", Product.Category.MEAT, 1200.0, 4, LocalDate.now().plusDays(9), 0.0));
        productList.add(new Product(24, "Wheat Flour", Product.Category.GROCERIES, 70.0, 35, LocalDate.now().plusDays(8), 0.0));
        productList.add(new Product(25, "Coconut Oil", Product.Category.GROCERIES, 500.0, 10, LocalDate.now().plusDays(7), 20.0));

    }

    public List<Product> getAll() {
        return new ArrayList<>(productList);
    }

    public void create(Product product) {
        productList.add(product);
    }

    public void create2(List<Product> products) {
        productList.addAll(products);
    }

    public Product get(int id) {
        return productList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean update(Product updatedProduct) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId() == updatedProduct.getId()) {
                productList.set(i, updatedProduct);
                return true;
            }
        }
        return false;
    }

    public boolean delete(int id) {
        return productList.removeIf(p -> p.getId() == id);
    }

    public List<Product> getProductsByCategory(Product.Category category) {
        return productList.stream()
                .filter(p -> p.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Product> getExpiringInNextDays(int days) {
        LocalDate now = LocalDate.now();
        return productList.stream()
                .filter(p -> p.getExpiryDate() != null &&
                        !p.getExpiryDate().isBefore(now) &&
                        !p.getExpiryDate().isAfter(now.plusDays(days)))
                .collect(Collectors.toList());
    }

    public List<Product> getExpiredProducts() {
        LocalDate now = LocalDate.now();
        return productList.stream()
                .filter(p -> p.getExpiryDate() != null && p.getExpiryDate().isBefore(now))
                .collect(Collectors.toList());
    }

    public Map<Product.Category, Double> getTotalValueGroupedByCategory() {
        return productList.stream()
                .filter(p -> p.getExpiryDate() == null || !p.getExpiryDate().isBefore(LocalDate.now()))
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.summingDouble(p -> {
                            double effectivePrice = p.getPrice();
                            if (p.getDiscount() != null && p.getDiscount() > 0) {
                                effectivePrice = effectivePrice * (1 - p.getDiscount() / 100);
                            }
                            return effectivePrice * p.getQuantity();
                        })
                ));
    }

    public Map<Product.Category, List<Product>> getDiscountedProductsByCategory() {
        return productList.stream()
                .filter(p -> p.getDiscount() != null && p.getDiscount() > 0)
                .collect(Collectors.groupingBy(Product::getCategory));
    }
}


