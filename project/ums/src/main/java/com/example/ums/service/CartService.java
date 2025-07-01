package com.example.ums.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;



    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Add a product to the cart
    public Object addProductToCart(Long id1,Long id2, Cart cart) {
        User user = userRepository.findById(id1);
        Product product = productRepository.findById(id2);
       // Save cart item in the database
        return cartRepository.save(user,product, cart);
    }

//    // Add multiple products to the cart
//    public Object addMultipleProductsToCart(Long userId, List<Product> products) {
//        Optional<User> userOpt = userRepository.findById(userId);
//
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            for (Product product : products) {
//                Cart cart = new Cart();
//                cart.setUser(user);
//                cart.setProduct(product);
//                cartRepository.save2(cart);  // Save each product to the cart
//            }
//            return 1;  // Success
//        }
//
//        return 0;  // Failure (User not found)
//    }

    // Update the quantity of a product in the cart
    public Cart updateProductQuantity(Long id, Cart cart) {
        cartRepository.update(id, cart); // Update the product quantity in the cart
        return cart;
    }

    // Remove a product from the cart
    public void removeProductFromCart(Long id) {
        cartRepository.delete(id); // Remove the cart item by ID
    }

    // Get all cart items for a specific user
    public List<Cart> getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId); // Get all cart items for a user
    }





}
