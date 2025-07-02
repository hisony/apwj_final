package com.example.ums.repository;

import com.example.ums.entity.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find a product by ID
    public Product findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Product.class));
    }

    // Get all products
    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }

    // Save a new product
    public int save(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock_quantity, category_id, image_url,expiry_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getPrice(),
                product.getStockQuantity(), product.getCategory().getId(), product.getImageUrl());
    }

    // Update an existing product
    public int update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ?, category_id = ?, image_url = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getPrice(),
                product.getStockQuantity(), product.getCategory().getId(), product.getImageUrl(), product.getId());
    }

    public int updateDiscount(Product product) {
        String sql = "UPDATE products SET price = ?, discount = ?, expiry_date = ? WHERE id = ?";
        return jdbcTemplate.update(sql, product.getPrice(), product.getDiscount(), product.getExpiryDate(), product.getId());
    }

    // Delete a product by ID
    public int delete(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }



    // Get products near expiry
    public List<Product> findProductsNearExpiry() {
        String sql = """
            SELECT 
                p.id, 
                p.name, 
                p.price, 
                p.expiry_date
            FROM 
                products p
            WHERE 
                p.expiry_date <= CURDATE() + INTERVAL 7 DAY
                AND p.expiry_date >= CURDATE();
        """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }
}
