package com.example.UniversityManagement.services;


import com.example.UniversityManagement.entity.Authority;
import com.example.UniversityManagement.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    // Get all authorities
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    // Get authority by ID
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findById(id);
    }

    // Create a new authority
    public void createAuthority(Authority authority) {
        if (authority.getUsername() == null || authority.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (authority.getAuthority() == null || authority.getAuthority().trim().isEmpty()) {
            throw new IllegalArgumentException("Authority cannot be empty");
        }

        authorityRepository.save(authority);
    }

    // Update an existing authority
    public void updateAuthority(Long id, Authority authority) {
        Authority existingAuthority = authorityRepository.findById(id);
        if (existingAuthority == null) {
            throw new IllegalArgumentException("Authority not found with id: " + id);
        }

        authority.setId(id);
        authorityRepository.save(authority);
    }

    // Delete authority by ID
    public void deleteAuthority(Long id) {
        Authority existingAuthority = authorityRepository.findById(id);
        if (existingAuthority == null) {
            throw new IllegalArgumentException("Authority not found with id: " + id);
        }

        authorityRepository.deleteById(id);
    }

    // Find authority by username
    public Authority getAuthorityByUsername(String username) {
        return authorityRepository.findByUsername(username);
    }

    // Find authority by username and authority
    public Authority getAuthorityByUsernameAndAuthority(String username, String authority) {
        return authorityRepository.findByUsernameAndAuthority(username, authority);
    }
}
