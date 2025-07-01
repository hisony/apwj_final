package com.example.ums.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Add a product to the wishlist


    public Object addProductToWishlist(Long id1,Long id2, Wishlist wishlist) {
        User user = userRepository.findById(id1);
        Product product = productRepository.findById(id2);
        // Save cart item in the database
        return wishlistRepository.save(user,product, wishlist);
    }

    // Remove a product from the wishlist
    public void removeProductFromWishlist(Long id) {
        wishlistRepository.delete(id); // Remove the wishlist item by ID
    }

    // Get all wishlist items for a specific user
    public List<Wishlist> getWishlistByUser(Long userId) {
        return wishlistRepository.findByUserId(userId); // Get all wishlist items for a user
    }
}

