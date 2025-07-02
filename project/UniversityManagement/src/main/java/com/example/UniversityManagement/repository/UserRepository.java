package com.example.UniversityManagement.repository;

import com.example.UniversityManagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find all users
    public List<User> findAll() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM user");
        List<User> users = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            User user = new User();
            user.setUsername((String) row.get("username"));
            user.setPassword((String) row.get("password"));
            user.setEnabled((Boolean) row.get("enabled"));
            users.add(user);
        }
        return users;
    }

    // Find a user by username (primary key)
    public User findByUsername(String username) {
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM user WHERE username = ?", username);
            User user = new User();
            user.setUsername((String) row.get("username"));
            user.setPassword((String) row.get("password"));
            user.setEnabled((Boolean) row.get("enabled"));
            return user;
        } catch (Exception e) {
            return null; // User not found
        }
    }



    // Save or update a user
    public void save(User user) {
        // Check if user exists
        User existingUser = findByUsername(user.getUsername());
        
        if (existingUser == null) {
            // Insert new user
            jdbcTemplate.update(
                    "INSERT INTO user (username, password, enabled) VALUES (?, ?, ?)",
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled()
            );
        } else {
            // Update existing user
            jdbcTemplate.update(
                    "UPDATE user SET password = ?, enabled = ? WHERE username = ?",
                    user.getPassword(),
                    user.isEnabled(),
                    user.getUsername()
            );
        }
    }

    public void updatePass(User user) {
        String query = "UPDATE user SET password = ? WHERE username = ?";
        jdbcTemplate.update(query, user.getPassword(), user.getUsername());
    }

    // Delete user by username
    public void deleteByUsername(String username) {
        jdbcTemplate.update("DELETE FROM user WHERE username = ?", username);
    }

    // Check if user exists by username
    public boolean existsByUsername(String username) {
        try {
            jdbcTemplate.queryForMap("SELECT username FROM user WHERE username = ?", username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
