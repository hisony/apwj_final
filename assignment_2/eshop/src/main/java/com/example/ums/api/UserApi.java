package com.example.ums.api;

import com.example.ums.entity.User;
import com.example.ums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserApi {

    private final UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    // Endpoint for user registration (sign-up)
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Register the user and return the saved user object
        userService.registerUser(user);  // Ensure password hashing occurs inside registerUser method
        return user;  // Returning the user after registration
    }

    // Endpoint for getting user details by username
    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);  // Fetch user by username
    }

    // Endpoint for getting all users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();  // Get all users from the database
    }

}


