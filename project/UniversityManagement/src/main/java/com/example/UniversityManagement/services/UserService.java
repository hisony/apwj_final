package com.example.UniversityManagement.services;

import com.example.UniversityManagement.entity.User;
import com.example.UniversityManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by username
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
        return user;
    }

    // Create a new user
    public void createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Check if user already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with username '" + user.getUsername() + "' already exists");
        }

        // Hash?
        userRepository.save(user);
    }

    // Update an existing user
    public void updateUser(String username, User user) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        // Ensure the username in the user object matches the path parameter
        user.setUsername(username);
        userRepository.save(user);
    }

    // Delete a user by username
    public void deleteUser(String username) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        userRepository.deleteByUsername(username);
    }

    public void resetPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            //user.setPassword(passwordEncoder.encode(newPassword)); // Encode the new password
            userRepository.save(user); // Save the updated user back to the database
        }
    }

    // Disable or enable user account
    public void toggleUserEnabled(String username, boolean enabled) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        existingUser.setEnabled(enabled);
        userRepository.save(existingUser);
    }
}
