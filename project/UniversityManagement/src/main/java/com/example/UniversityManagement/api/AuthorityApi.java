package com.example.UniversityManagement.api;

import com.example.UniversityManagement.entity.Authority;
import com.example.UniversityManagement.services.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityApi {

    private final AuthorityService authorityService;

    @Autowired
    public AuthorityApi(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    // Get all authorities
    @GetMapping
    public ResponseEntity<List<Authority>> getAllAuthorities() {
        List<Authority> authorities = authorityService.getAllAuthorities();
        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }

    // Get authority by ID
    @GetMapping("/{id}")
    public ResponseEntity<Authority> getAuthorityById(@PathVariable("id") Long id) {
        try {
            Authority authority = authorityService.getAuthorityById(id);
            return new ResponseEntity<>(authority, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get authority by username
    @GetMapping("/username/{username}")
    public ResponseEntity<Authority> getAuthorityByUsername(@PathVariable("username") String username) {
        Authority authority = authorityService.getAuthorityByUsername(username);
        if (authority == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(authority, HttpStatus.OK);
    }

    // Get authority by username and authority
    @GetMapping("/username/{username}/authority/{authority}")
    public ResponseEntity<Authority> getAuthorityByUsernameAndAuthority(
            @PathVariable("username") String username,
            @PathVariable("authority") String authority) {
        Authority authorityResult = authorityService.getAuthorityByUsernameAndAuthority(username, authority);
        if (authorityResult == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(authorityResult, HttpStatus.OK);
    }

    // Create a new authority
    @PostMapping
    public ResponseEntity<String> createAuthority(@RequestBody Authority authority) {
        try {
            authorityService.createAuthority(authority);
            return new ResponseEntity<>("Authority created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update an existing authority
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAuthority(
            @PathVariable("id") Long id, @RequestBody Authority authority) {
        try {
            authorityService.updateAuthority(id, authority);
            return new ResponseEntity<>("Authority updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete authority by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthority(@PathVariable("id") Long id) {
        try {
            authorityService.deleteAuthority(id);
            return new ResponseEntity<>("Authority deleted successfully", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
