package com.example.ums.repository;

import com.example.ums.entity.Cart;
import com.example.ums.entity.Product;
import com.example.ums.entity.User;
import com.example.ums.entity.Wishlist;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishlistRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get all wishlist items for a user
    public List<Wishlist> findByUserId(Long userId) {
        String sql = "SELECT * FROM wishlist WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(Wishlist.class));
    }

    // Add a product to the wishlist
    public Object save(User user, Product product, Wishlist wishlist) {

        System.out.println(user.getId()+" "+ product.getId());
        String sql = "INSERT INTO wishlist (user_id, product_id, created_at) VALUES (?, ?,  ?)";
        return jdbcTemplate.update(sql,user.getId(), product.getId(),  wishlist.getCreatedAt());
    }

    // Remove a product from the wishlist
    public int delete(Long id) {
        String sql = "DELETE FROM wishlist WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

