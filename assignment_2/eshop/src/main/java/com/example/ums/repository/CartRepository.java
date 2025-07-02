package com.example.ums.repository;


import com.example.ums.entity.Cart;
import com.example.ums.entity.Category;
import com.example.ums.entity.User;
import com.example.ums.entity.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cart> findByUserId(Long userId) {
        String sql = """
                SELECT 
                    c.id AS cart_id, 
                    c.user_id, 
                    c.product_id, 
                    c.quantity, 
                    c.created_at,
                    u.username, 
                    u.password, 
                    u.email, 
                    u.role, 
                    u.first_name, 
                    u.last_name, 
                    u.address, 
                    u.phone_number, 
                    u.created_at AS user_created_at,
                    p.name AS product_name, 
                    p.description AS product_description, 
                    p.price, 
                    p.stock_quantity, 
                    p.category_id, 
                    p.image_url AS product_image_url,
                    p.created_at AS product_created_at
                FROM 
                    carts c
                LEFT JOIN 
                    users u ON c.user_id = u.id
                LEFT JOIN 
                    products p ON c.product_id = p.id
                WHERE 
                    c.user_id = ?;
            """;

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            // Map Cart
            Cart cart = new Cart();
            cart.setId(rs.getLong("cart_id"));
            cart.setQuantity(rs.getInt("quantity"));
            cart.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            // Map User
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));

            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setAddress(rs.getString("address"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setCreatedAt(rs.getTimestamp("user_created_at").toLocalDateTime());
            cart.setUser(user);

            // Map Product
            Product product = new Product();
            product.setId(rs.getLong("product_id"));
            product.setName(rs.getString("product_name"));
            product.setDescription(rs.getString("product_description"));
            product.setPrice(rs.getDouble("price"));
            product.setStockQuantity(rs.getInt("stock_quantity"));// Assuming category_id is the category ID
            product.setImageUrl(rs.getString("product_image_url"));
            product.setCreatedAt(rs.getTimestamp("product_created_at").toLocalDateTime());
            cart.setProduct(product);

            return cart;
        });
    }






    // Add a product to the cart
    public Object save(User user,Product product, Cart cart) {

        System.out.println(user.getId()+" "+ product.getId());
        String sql = "INSERT INTO carts (user_id, product_id, quantity,created_at) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,user.getId(), product.getId(), cart.getQuantity(), cart.getCreatedAt());
    }

//    public int save2(Cart cart) {
//        String sql = "INSERT INTO carts (user_id, product_id, quantity, created_at) VALUES (?, ?, ?, ?)";
//        return jdbcTemplate.update(sql, cart.getUser().getId(), cart.getProduct().getId(), cart.getQuantity(), cart.getCreatedAt());
//    }

    // Update quantity of a product in the cart
    public int update(Long id, Cart cart) {
        String sql = "UPDATE carts SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, cart.getQuantity(), id);
    }

    // Remove a product from the cart
    public int delete(Long id) {
        String sql = "DELETE FROM carts WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
