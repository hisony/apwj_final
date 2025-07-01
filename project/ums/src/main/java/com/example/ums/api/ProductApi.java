package com.example.ums.api;



import com.example.ums.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApi {

    private final ProductService productService;

    @Autowired
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    // Endpoint to get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Endpoint to get a product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Endpoint to create a new product (Admin only)

    @PostMapping("/register")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // Endpoint to update a product (Admin only)

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    // Endpoint to delete a product (Admin only)

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/discounts")
    public List<Product> getDiscountedProducts() {
        return productService.getDiscountForExpiringProducts();
    }
}
