package com.example.legacysample.api;

import com.example.legacysample.entity.Product;
import com.example.legacysample.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductApi {

    private final ProductService productService;

    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all products, optionally filtered by category.
     *
     * @param category The category to filter by (optional).
     * @return A list of products.
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAll(@RequestParam(required = false) Product.Category category) {
        if (category != null) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }
        return ResponseEntity.ok(productService.getAll());
    }

    /**
     * Get a single product by its ID.
     *
     * @param id The ID of the product.
     * @return The product, or a 404 if not found.
     */
    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") int id) {
        return productService.get(id);

    }


    /**
     * Get products that are expiring in the next 7 days with applied discounts.
     *
     * @return A list of products expiring soon with discounts.
     */
    @GetMapping("/products/expiring-soon")
    public List<Product> getExpiringInNext7Days() {

        return productService.getExpiringInNext7Days();
    }

    /**
     * Generate a report showing total value (price) of all available products, grouped by category.
     *
     * @return A map of categories with the total price for each category.
     */
    @GetMapping("/products/total-value")
    public Map<Product.Category, Double> getTotalValueGroupedByCategory() {

        return productService.getTotalValueGroupedByCategory();
    }

    /**
     * Generate a report showing products by category with applied discounts (if any).
     *
     * @return A map of categories with the list of discounted products.
     */
    @GetMapping("/products/discounted-by-category")
    public Map<Product.Category, List<Product>> getDiscountedProductsByCategory() {
        return productService.getDiscountedProductsByCategory();

    }
}

