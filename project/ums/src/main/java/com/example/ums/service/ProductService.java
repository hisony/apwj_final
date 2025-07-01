package com.example.ums.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // Fetch all products from the database
    }

    // Get a product by its ID
    public Product getProductById(Long id) {
        return productRepository.findById(id); // Fetch product by ID
    }

    // Create a new product
    public Product createProduct(Product product) {
        productRepository.save(product); // Save product in the database
        return product;
    }

    // Update a product
    public Product updateProduct(Long id, Product product) {
        product.setId(id); // Update the existing product
        productRepository.update(product); // Update the product in the database
        return product;
    }

    // Delete a product by its ID
    public void deleteProduct(Long id) {
        productRepository.delete(id); // Delete the product by ID
    }

    public List<Product> getDiscountForExpiringProducts() {
        // Fetch all products
        List<Product> products = productRepository.findAll();

        // List to store products that have a discount applied
        List<Product> discountedProducts = new ArrayList<>();

        // Loop through all products and apply discount if the expiry date is near
        for (Product product : products) {
            if (product.getExpiryDate() != null) {
                LocalDate currentDate = LocalDate.now();
                long daysToExpiry = java.time.temporal.ChronoUnit.DAYS.between(currentDate, product.getExpiryDate());

                // Apply 20% discount if expiry is within 7 days
                if (daysToExpiry <= 7) {
                    double discountedPrice = product.getPrice() * 0.8; // 20% discount
                    product.setDiscount(20.0);  // Set discount percentage
                    product.setPrice(discountedPrice);  // Update price with discount

                    // Save updated product to database
                    productRepository.updateDiscount(product);

                    // Add to the list of discounted products
                    discountedProducts.add(product);
                }
            }
        }

        // Return only the products with discounts applied
        return discountedProducts;
    }



}
