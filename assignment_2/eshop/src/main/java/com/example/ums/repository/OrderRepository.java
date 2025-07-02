package com.example.ums.repository;


import com.example.ums.entity.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Order findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Order.class));
    }

    // Get all orders for a user
    public List<Order> findByUserId(Long userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(Order.class));
    }

    // Get all orders (admin)
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    // Save an order
    public int save(Order order) {
        String sql = "INSERT INTO orders (user_id, total_price, order_date, status, shipping_address) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, order.getUser().getId(), order.getTotalPrice(), order.getOrderDate(),
                order.getStatus().toString(), order.getShippingAddress());
    }
}
