package com.example.ums.api;

import com.example.ums.service.SalesReportService;
import com.example.ums.entity.SalesReport;
import com.example.ums.service.ProductService;
import com.example.ums.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminApi
{

    private final SalesReportService salesReportService;
    private final ProductService productService;

    @Autowired
    public AdminApi(SalesReportService salesReportService, ProductService productService) {
        this.salesReportService = salesReportService;
        this.productService = productService;
    }


    @GetMapping("/best-selling-products")
    public List<SalesReport> getBestSellingProducts() {
        return salesReportService.getBestSellingProducts();
    }


    @GetMapping("/sales-report")
    public List<SalesReport> getMonthlySalesReport() {
        return salesReportService.getMonthlySalesReport();
    }

    // Endpoint to manage products (Admin only)

    @PostMapping("/product")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }


    @PutMapping("/product/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }


    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
