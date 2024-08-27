package com.revature.eeecommerce.Product;

import com.revature.eeecommerce.util.exceptions.DataNotFoundException;
import com.revature.eeecommerce.util.exceptions.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** CONTROLLER CLASS DOCUMENTATION
 * @author Arjun Ramsinghani
 * The Controller class will interact with the front end part of our application and send the data coming through there through our Service class for processing and validation.
 * Instructions on how each method should interact are within each method call.
 */

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    /**
     * @param productService - a service object to pass what we want to do through the Service layer
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * @return - a list of all functions
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            throw new DataNotFoundException("No products found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    /**
     * @param id - the primary key we will use to find the product
     * @return - a product with the associated primary key
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@Valid @PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id));
    }

    /**
     * @param newProduct - a new product
     * @param userType - the user's enum type
     * @return - a new product that the employees wants to sell
     */
    @PostMapping
    public ResponseEntity<Product> postNewProduct(@RequestBody Product newProduct, @RequestHeader String userType) {
        if (!userType.equals("EMPLOYEE")) {
            throw new UnauthorizedException("You are not logged in as a Seller");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(newProduct));
    }

    /**
     * @param updatedProduct - a product we want to update
     * @param userType - the user's enum type
     * @return - true
     */
    @PutMapping
    public ResponseEntity<Boolean> putUpdateProduct(@Valid @RequestBody Product updatedProduct, @RequestHeader String userType) {
        if (!userType.equals("EMPLOYEE")) {
            throw new UnauthorizedException("You are not logged in as a Seller");
        }

        boolean updateProduct = productService.updateProduct(updatedProduct);

        if (updateProduct == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    /**
     * @param product_id - the primary key of a product we want to delete
     * @param userType - the user's enum type
     * @return - true
     */
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable int product_id, @RequestHeader String userType) {
        if (!userType.equals("EMPLOYEE")) {
            throw new UnauthorizedException("You are not logged in as a Seller");
        }

        productService.deleteProduct(product_id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
