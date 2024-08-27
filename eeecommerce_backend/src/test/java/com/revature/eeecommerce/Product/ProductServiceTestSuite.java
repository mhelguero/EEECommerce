package com.revature.eeecommerce.Product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTestSuite {
    @Mock // indicates Object needing to be mocked
    private ProductRepository mockProductRepository;

    @InjectMocks // injects our ProductRepository into our ProductService as a mocked object
    private ProductService sut;

    @Test
    public void testGetAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(2, 2400, 0.2, "E Shirt", "Let everyone know you love the letter E", 200, "https://th.bing.com/th/id/OIG2.L4QTYfR6oNyS5Jq.QasC?w=270&h=270&c=6&r=0&o=5&pid=ImgGn")
        );
        when(mockProductRepository.findAll()).thenReturn(products);
        List<Product> result = sut.getAllProducts();
        assertEquals(1, result.size());
        verify(mockProductRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        Product products = new Product(2, 2400, 0.2, "E Shirt", "Let everyone know you love the letter E", 200, "https://th.bing.com/th/id/OIG2.L4QTYfR6oNyS5Jq.QasC?w=270&h=270&c=6&r=0&o=5&pid=ImgGn");
        when(mockProductRepository.findById(products.getProduct_id())).thenReturn(Optional.of(products));

        Product result = sut.findById(products.getProduct_id());

        // Verify that all parameters are not null
        assertNotNull(result);
        assertEquals(products.getProduct_id(), result.getProduct_id());
        assertEquals(products.getName(), result.getName());
        assertEquals(products.getPrice(), result.getPrice());
        assertEquals(products.getDescription(), result.getDescription());
        assertEquals(products.getQuantity(), result.getQuantity());
        assertEquals(products.getImage(), result.getImage());
        assertEquals(products, result);

        verify(mockProductRepository, times(1)).findById(products.getProduct_id());
    }

    @Test
    public void testCreateProduct() {
        Product validProduct = new Product(1, 2400, 0.2, "E Shirt", "Let everyone know you love the letter E", 200, "https://th.bing.com/th/id/OIG2.L4QTYfR6oNyS5Jq.QasC?w=270&h=270&c=6&r=0&o=5&pid=ImgGn");
        when(mockProductRepository.save(validProduct)).thenReturn(validProduct);

        Product returnedProduct = sut.createProduct(validProduct);

        assertEquals(validProduct, returnedProduct);
        verify(mockProductRepository, times(1)).save(validProduct);
    }

    @Test
    public void testUpdateProduct_ValidQuantity() {
        Product validProduct = new Product(1, 2599, 0.22, "E or e Shirt", "Let everyone know you love the letter E and e", 200, "https://th.bing.com/th/id/OIG2.L4QTYfR6oNyS5Jq.QasC?w=270&h=270&c=6&r=0&o=5&pid=ImgGn");
        when(mockProductRepository.save(validProduct)).thenReturn(validProduct);

        boolean returnedUpdate = sut.updateProduct(validProduct);
        assertTrue(returnedUpdate);
        verify(mockProductRepository, times(1)).save(validProduct);
    }

    @Test
    public void testUpdateProduct_InvalidQuantity() {
        Product validProduct = new Product(1, 2599, 0.22, "E or e Shirt", "Let everyone know you love the letter E and e", -10, "https://th.bing.com/th/id/OIG2.L4QTYfR6oNyS5Jq.QasC?w=270&h=270&c=6&r=0&o=5&pid=ImgGn");
        //when(mockProductRepository.save(validProduct)).thenReturn(validProduct);

        boolean returnedUpdate = sut.updateProduct(validProduct);
        assertFalse(returnedUpdate);
        verify(mockProductRepository, times(0)).save(validProduct);
    }

    @Test
    public void testDeleteProduct() {
        int product_id = 1;
        boolean actual = sut.deleteProduct(product_id);
        assertTrue(actual);
        verify(mockProductRepository, times(1)).deleteById(1);
    }
}
