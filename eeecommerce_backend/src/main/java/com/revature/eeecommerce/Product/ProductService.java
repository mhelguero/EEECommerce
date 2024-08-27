package com.revature.eeecommerce.Product;

import com.revature.eeecommerce.util.exceptions.DataNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** SERVICE CLASS DOCUMENTATION
 * @author Arjun Ramsinghani
 * The Service class is used to define the business logic coming to the backend by use of Spring Boot and will communicate with the database through Spring Data.
 * Instructions on how each method should interact are within each method call.
 */

@Service
public class ProductService {
    private ProductRepository productRepository;

    /**
     * @param productRepository - gets the product repository so that we are able to perform our db functions
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * @return - a list of all the products
     */
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return null;
        }

        else {
            return products;
        }
    }

    /**
     * @param product_id - our product's primary key
     * @return - a product
     */
    public Product findById(int product_id) {
        Product foundProduct = productRepository.findById(product_id).orElseThrow(() -> new DataNotFoundException("No product with the ID: " + product_id));
        return foundProduct;
    }

    /**
     * @param newProduct - a product object
     * @return - a brand new product
     */
    public Product createProduct(Product newProduct) {
        return productRepository.save(newProduct);
    }

    /**
     * @param updateProduct - a product that exists
     * @return - true
     */
    @Transactional
    public boolean updateProduct(Product updateProduct) {
        if (updateProduct.getQuantity() < 0) {
            return false;
        }

        productRepository.save(updateProduct);
        return true;
    }

    /**
     * @param product_id - the primary key of a product
     * @return - true
     */
    public boolean deleteProduct(int product_id) {
        productRepository.deleteById(product_id);
        return true;
    }
}
