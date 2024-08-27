package com.revature.eeecommerce.Cart;

import com.revature.eeecommerce.Product.Product;
import com.revature.eeecommerce.Product.ProductService;
import com.revature.eeecommerce.User.User;
import com.revature.eeecommerce.User.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.revature.eeecommerce.User.User.userType.CUSTOMER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIntegrationTestSuite {
    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    @Autowired
    private CartController cartController;

    @Autowired
    private MockMvc mockMvc;

    private static User defaultUser = new User(2, "Donkey", "Kong", "1 Hacker Way", "test@email.com", "Password@123", CUSTOMER);
    private static String userJSON = "{\"userId\":2,\"firstName\":\"Donkey\",\"lastName\":\"Kong\",\"address\":\"1 Hacker Way\",\"email\":\"test@email.com\",\"password\":\"Password@123\",\"userType\":\"CUSTOMER\"}";
    private static Product defaultProduct = new Product(2, 2400, 0.25, "E Shirt", "Let everyone know you love the letter E", 200, "image:url");
    private static String productJSON = "{\"product_id\":2,\"price\":2400,\"discount\":0.25,\"name\":\"E Shirt\",\"description\":\"Let everyone know you love the letter E\",\"quantity\":200,\"image\":\"image:url\"}";

    private static Cart defaultCart = new Cart(1, defaultUser, defaultProduct, 1);

    private static String cartJSON = "{\"cartId\":1,\"user\":" + userJSON + ",\"product\":" + productJSON + ",\"count\":1}";

    @Test
    public void testGetCartByUserId() throws Exception {
        when(cartService.findCartByUserId(2)).thenReturn(List.of(defaultCart));
        String expectedResult = cartJSON;

        mockMvc.perform(MockMvcRequestBuilders.get("/cart")
                .header("userId", defaultUser.getUserId())
                .header("userType", defaultUser.getUserType().name()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string("[" + expectedResult + "]"));
    }

    @Test
    public void testPostCart() throws Exception {
        String expectedJSON = cartJSON;
        when(cartService.postCart(any(Cart.class))).thenReturn(defaultCart);
        when(userService.findById(defaultUser.getUserId())).thenReturn(defaultUser);
        when(productService.findById(defaultProduct.getProduct_id())).thenReturn(defaultProduct);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart?productId=2&quantity=3")
                .header("userId", defaultUser.getUserId()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string(expectedJSON));
    }

    @Test
    public void testDeleteCart() throws Exception {
        when(cartService.deleteCart(defaultCart.getCartId())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/" + defaultCart.getCartId()))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

}
