package com.revature.eeecommerce.Cart;

import com.revature.eeecommerce.Product.Product;
import com.revature.eeecommerce.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.revature.eeecommerce.User.User.userType.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTestSuite {
    @Mock
    private CartRepository mockCartRepository;

    @InjectMocks
    private CartService sut;

    private static User defaultUser = new User(2, "Donkey", "Kong", "1 Hacker Way", "test@email.com", "Password@123", CUSTOMER);
    private static Product defaultProduct = new Product(2, 2400, 0.2, "E Shirt", "Let everyone know you love the letter E", 200, "https://th.bing.com/th/id/OIG2.L4QTYfR6oNyS5Jq.QasC?w=270&h=270&c=6&r=0&o=5&pid=ImgGn");
    private static Cart validCart = new Cart(1, defaultUser, defaultProduct, 1);

    @Test
    public void testGetAllCarts() {
        List<Cart> carts = new ArrayList<>(Arrays.asList(validCart));
        when(mockCartRepository.findAll()).thenReturn(carts);

        List<Cart> result = sut.findAllCarts();
        assertEquals(1, result.size());
        assertEquals(validCart, result.get(0));
    }

    @Test
    public void testGetCartByUserId() {
        List<Cart> carts = new ArrayList<>(Arrays.asList(validCart));
        when(mockCartRepository.findCartByUserUserId(defaultUser.getUserId())).thenReturn(carts);

        List<Cart> result = sut.findCartByUserId(defaultUser.getUserId());
        assertEquals(validCart, result.get(0));
    }

    @Test
    public void testAddItemsToCart() {
        when(mockCartRepository.save(validCart)).thenReturn(validCart);

        Cart returnedCart = sut.postCart(validCart);

        assertEquals(validCart, returnedCart);
        verify(mockCartRepository, times(1)).save(validCart);
    }

    @Test
    public void testDeleteCart() {
        boolean actual = sut.deleteCart(validCart.getCartId());
        assertTrue(actual);
        verify(mockCartRepository, times(1)).deleteById(1);
    }
}
