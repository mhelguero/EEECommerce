package com.revature.eeecommerce.Order;

import com.revature.eeecommerce.Cart.Cart;
import com.revature.eeecommerce.Cart.CartService;
import com.revature.eeecommerce.OrderItem.OrderItem;
import com.revature.eeecommerce.OrderItem.OrderItemService;
import com.revature.eeecommerce.Product.Product;
import com.revature.eeecommerce.Product.ProductService;
import com.revature.eeecommerce.User.User;
import com.revature.eeecommerce.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import static com.revature.eeecommerce.User.User.userType.CUSTOMER;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTestSuite {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderService orderService;
    @MockBean
    private UserService userService;
    @MockBean
    private CartService cartService;
    @MockBean
    private OrderItemService orderItemService;
    @MockBean
    private ProductService productService;

    @Autowired
    private OrderController orderController;

    @Autowired
    private MockMvc mockMvc;

    private static Time now = Time.valueOf(LocalTime.now());
    private static User defaultUser = new User(1, "Amsal", "Kassam", "12345 Fake St.", "test@email.com", "Password123", User.userType.EMPLOYEE);
    private static String userJSON = "{\"userId\":1,\"firstName\":\"Amsal\",\"lastName\":\"Kassam\",\"address\":\"12345 Fake St.\",\"email\":\"test@email.com\",\"password\":\"Password123\",\"userType\":\"EMPLOYEE\"}";
    private static Product defaultProduct = new Product(1, 100, 0.0, "testProduct", "", 100, "image:url");

    private static Order defaultOrder = new Order(1, defaultUser, now);
    private static String orderJSON = "{\"orderId\":1,\"user\":" + userJSON + ",\"date\":\"" + now.toString() + "\"}";

    @Test
    public void testFailCheckout() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/orders"))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testSuccessCheckout() throws Exception {
        String expectedJSON = "[{\"orderItemId\":0,\"order\":null,\"product\":{\"product_id\":1,\"price\":100,\"discount\":0.0,\"name\":\"testProduct\",\"description\":\"\",\"quantity\":99,\"image\":\"image:url\"},\"count\":1,\"discount\":0.0}]";
        when(cartService.findCartByUserId(1)).thenReturn(List.of(new Cart(1, defaultUser, defaultProduct, 1)));
        when(userService.findById(1)).thenReturn(defaultUser);
        when(orderService.create(defaultOrder)).thenReturn(defaultOrder);
        when(orderItemService.create(any(OrderItem.class))).thenReturn(new OrderItem());
        when(cartService.deleteCart(anyInt())).thenReturn(true);
        when(productService.updateProduct(any(Product.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .header("userId", 1))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.content().string(expectedJSON));
    }

    @Test
    public void testFindOrder() throws Exception {
        when(orderService.findById(1)).thenReturn(defaultOrder);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string(orderJSON));
    }

    @Test
    public void testFindAllOrders() throws Exception {
        when(orderService.findAll()).thenReturn(List.of(defaultOrder));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string("[" + orderJSON + "]"));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        when(orderService.update(defaultOrder)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(orderJSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    public void testGetOrdersByUserId() throws Exception {
        when(orderService.findAllById(1)).thenReturn(List.of(defaultOrder));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/user/1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().string("[" + orderJSON + "]"));
    }
}
