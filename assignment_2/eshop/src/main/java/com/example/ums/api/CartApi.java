package com.example.ums.api;



import com.example.ums.entity.Cart;
import com.example.ums.entity.Product;
import com.example.ums.service.CartService;
import com.example.ums.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartApi {

    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartApi(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService=productService;
    }

    // Endpoint to add a product to the cart

    @PostMapping("/add/{id1}/{id2}")
    public String addProductToCart(@PathVariable Long id1, @PathVariable Long id2, @RequestBody Cart cart) {
        Object x= cartService.addProductToCart(id1, id2, cart);
        if(x.equals(1)){
            return "Added product successfully";
        }
        return "Unable to add to cart";
    }

//    @PostMapping("/add/bulk/{id}")
//    public String addMultipleProductsToCart(@PathVariable Long id,@RequestBody List<Product> products) {
//
//        Object result = cartService.addMultipleProductsToCart(id, products);
//        if (result.equals(1)) {
//            return "Added products successfully";
//        }
//        return "Unable to add products to cart";
//    }


    // Endpoint to update product quantity in the cart

    @PutMapping("/update/{id}")
    public String updateProductQuantity(@PathVariable Long id, @RequestBody Cart cart) {
        Object x= cartService.updateProductQuantity(id, cart);

        if(x.equals(1)){
            return "Updated product successfully";
        }
        return "Unable to update";
    }

    // Endpoint to remove a product from the cart

    @DeleteMapping("/remove/{id}")
    public void removeProductFromCart(@PathVariable Long id) {
        cartService.removeProductFromCart(id);
    }

    // Endpoint to view all items in the user's cart

    @GetMapping("/user/{userId}")
    public List<Cart> getCartByUser(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }


}

