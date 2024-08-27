package com.revature.eeecommerce.Product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTestSuite {
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ProductService productService;
    @Autowired
    private ProductController productController;
    @Autowired
    private MockMvc mockMvc;

    private static Product validProduct = new Product(1, 2599, 0.25, "tEst product", "test product shirt with a giant E", 200, "image:url");
    private static String productJSON = "{\"product_id\":1,\"price\":2599,\"discount\":0.25,\"name\":\"tEst product\",\"description\":\"test product shirt with a giant E\",\"quantity\":200,\"image\":\"image:url\"}";

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(validProduct));
        String expectedResult = "[" + productJSON + "]";

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.findById(1)).thenReturn(validProduct);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string(productJSON));
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(validProduct)).thenReturn(validProduct);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .header("userType", "EMPLOYEE")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJSON))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().string(productJSON));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(validProduct)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/products")
                .header("userType", "EMPLOYEE")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.deleteProduct(1)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1")
                .header("userType", "EMPLOYEE")
                .contentType(productJSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}
