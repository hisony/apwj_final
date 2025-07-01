package com.example.ums.service;

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

    // Register a new user
    public void registerUser(User user) {
        userRepository.save(user); // Save the user in the database
    }

    // Find a user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username); // Fetch user by username from repository
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Get all users from the database
    }

}


