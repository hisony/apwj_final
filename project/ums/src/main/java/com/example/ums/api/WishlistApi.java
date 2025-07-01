package com.example.ums.api;



import com.example.ums.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistApi {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistApi(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // Endpoint to add a product to the wishlist


    @PostMapping("/add/{id1}/{id2}")
    public String addProductToWishlist(@PathVariable Long id1, @PathVariable Long id2, @RequestBody Wishlist wishlist) {
        Object x= wishlistService.addProductToWishlist(id1, id2, wishlist);
        if(x.equals(1)){
            return "Added product successfully";
        }
        return "Unable to add to wishlist";
    }
    // Endpoint to remove a product from the wishlist

    @DeleteMapping("/remove/{id}")
    public void removeProductFromWishlist(@PathVariable Long id) {
        wishlistService.removeProductFromWishlist(id);
    }

    // Endpoint to get all wishlist items for a user

    @GetMapping("/user/{userId}")
    public List<Wishlist> getWishlistByUser(@PathVariable Long userId) {
        return wishlistService.getWishlistByUser(userId);
    }
}

