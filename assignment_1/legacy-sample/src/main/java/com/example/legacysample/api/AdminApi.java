package com.example.legacysample.api;



import com.example.legacysample.entity.Product;
import com.example.legacysample.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AdminApi {

    private final ProductService productService;



    public AdminApi(ProductService productService) {
        this.productService = productService;
    }


    /**
     * Create a new product.
     * @param product The product to create.
     * @return The created product with a 201 status code.
     *
     *  @PostMapping("/admin/productsCreate")
     *     public void create(@RequestBody Product product) {
     *          productService.create(product);
     *
     *     }
     */

    @PostMapping("/admin/productsCreate")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product); // Send back the created product
    }

    @PostMapping("/admin/productsCreate/bulk")
    public void create2(@RequestBody List<Product> products) {
        productService.create2(products);
    }





    /**
     * Update an existing product by its ID.
     * @param id The ID of the product.
     * @param product The updated product.
     * @return 204 No Content status for successful update, 404 if not found.
     */
    @PutMapping("/admin/productsUpdate/{id}")
    public void update(@PathVariable("id") int id, @RequestBody Product product) {
        productService.update(product);

    }

    /**
     * Delete a product by its ID.
     * @param id The ID of the product to delete.
     * @return 204 No Content status for successful deletion, 404 if not found.
     */
    @DeleteMapping("/admin/productsDelete/{id}")
    public void delete(@PathVariable("id") int id) {
        productService.delete(id);

    }




}
