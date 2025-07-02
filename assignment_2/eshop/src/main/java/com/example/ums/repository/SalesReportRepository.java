package com.example.ums.repository;

import com.example.ums.entity.SalesReport;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalesReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public SalesReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get best-selling products (you can customize this query as needed)
    public List<SalesReport> findBestSellingProducts() {
        String sql = "SELECT p.name, SUM(oi.quantity) AS total_quantity " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "GROUP BY oi.product_id " +
                "ORDER BY total_quantity DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SalesReport.class));
    }

    public List<SalesReport> findAll() {
        String sql = "SELECT * FROM sales_report"; // Adjust the table name if necessary
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SalesReport.class));
    }
}
