package com.example.UniversityManagement.repository;


import com.example.UniversityManagement.entity.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Repository
public class AuthorityRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find all authorities
    public List<Authority> findAll() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM authority");
        List<Authority> authorities = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Authority authority = new Authority();
            authority.setId(((Number) row.get("id")).longValue());
            authority.setUsername((String) row.get("username"));
            authority.setAuthority((String) row.get("authority"));
            authorities.add(authority);
        }
        return authorities;
    }

    // Find authority by ID
    public Authority findById(Long id) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM authority WHERE id = ?", id);
        Authority authority = new Authority();
        authority.setId(((Number) row.get("id")).longValue());
        authority.setUsername((String) row.get("username"));
        authority.setAuthority((String) row.get("authority"));
        return authority;
    }

    // Save a new authority or update an existing one
    public void save(Authority authority) {
        if (authority.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO authority (username, authority) VALUES (?, ?)",
                    authority.getUsername(),
                    authority.getAuthority()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE authority SET username = ?, authority = ? WHERE id = ?",
                    authority.getUsername(),
                    authority.getAuthority(),
                    authority.getId()
            );
        }
    }

    // Delete authority by ID
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM authority WHERE id = ?", id);
    }

    // Find authority by username
    public Authority findByUsername(String username) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM authority WHERE username = ?", username);
        Authority authority = new Authority();
        authority.setId(((Number) row.get("id")).longValue());
        authority.setUsername((String) row.get("username"));
        authority.setAuthority((String) row.get("authority"));
        return authority;
    }

    // Find authority by username and authority
    public Authority findByUsernameAndAuthority(String username, String authorityName) {
        Map<String, Object> row = jdbcTemplate.queryForMap(
                "SELECT * FROM authority WHERE username = ? AND authority = ?", username, authorityName
        );
        Authority authority = new Authority();
        authority.setId(((Number) row.get("id")).longValue());
        authority.setUsername((String) row.get("username"));
        authority.setAuthority((String) row.get("authority"));
        return authority;
    }
}
