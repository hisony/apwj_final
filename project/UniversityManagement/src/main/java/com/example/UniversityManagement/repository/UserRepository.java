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
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM users");
        List<User> users = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            User user = new User();
            user.setId(((Number) row.get("id")).longValue());
            user.setUsername((String) row.get("username"));
            user.setPassword((String) row.get("password"));
            user.setEnabled((Boolean) row.get("enabled"));
            users.add(user);
        }
        return users;
    }

    // Find a user by ID
    public User findById(Long id) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM users WHERE id = ?", id);
        User user = new User();
        user.setId(((Number) row.get("id")).longValue());
        user.setUsername((String) row.get("username"));
        user.setPassword((String) row.get("password"));
        user.setEnabled((Boolean) row.get("enabled"));
        return user;
    }

    // Save or update a user
    public void save(User user) {
        if (user.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)",
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE users SET username = ?, password = ?, enabled = ? WHERE id = ?",
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(),
                    user.getId()
            );
        }
    }

    // Delete user by ID
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    // Find user by username
    public User findByUsername(String username) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM users WHERE username = ?", username);
        User user = new User();
        user.setId(((Number) row.get("id")).longValue());
        user.setUsername((String) row.get("username"));
        user.setPassword((String) row.get("password"));
        user.setEnabled((Boolean) row.get("enabled"));
        return user;
    }
}
