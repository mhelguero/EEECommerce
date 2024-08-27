package com.revature.eeecommerce.OrderItem;

import com.revature.eeecommerce.Order.Order;
import com.revature.eeecommerce.Product.Product;
import com.revature.eeecommerce.User.User;
import com.revature.eeecommerce.util.exceptions.OrderItemNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.revature.eeecommerce.User.User.userType.CUSTOMER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTestSuite {
    @Mock
    private OrderItemRepository mockOrderItemRepository;

    @InjectMocks
    private OrderItemService sut;
    private OrderItem orderItem;

    private static OrderItem validOrderItem = new OrderItem(1,
            new Order(1, new User(2, "King", "Kong", "Empire State Building", "daking@skull.com", "Password123", CUSTOMER), Time.valueOf("14:36:03")),
            new Product(2, 2400, 0.2, "E Shirt", "Let everyone know you love the letter E", 200, "https://th.bing.com/th/id/OIG2.L4QTYfR6oNyS5Jq.QasC?w=270&h=270&c=6&r=0&o=5&pid=ImgGn"), 1, 0);

    @Test
    public void testGetAllOrderItems() {
        List<OrderItem> orders = new ArrayList<>(Arrays.asList(validOrderItem));
        when(mockOrderItemRepository.findAll()).thenReturn(orders);

        List<OrderItem> result = sut.findAll();
        assertEquals(1, result.size());
        assertEquals(validOrderItem, result.get(0));
    }

    @Test
    public void testGetOrderItemByOrderId() throws OrderItemNotFoundException {
        when(mockOrderItemRepository.findAllByOrderId(validOrderItem.getOrderItemId())).thenReturn(List.of(validOrderItem));

        List<OrderItem> result = sut.findAllByOrderId(validOrderItem.getOrderItemId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(validOrderItem, result.get(0));
        verify(mockOrderItemRepository, times(1)).findAllByOrderId(validOrderItem.getOrderItemId());
    }

    @Test
    public void testCreateOrderItem() {
        when(mockOrderItemRepository.save(validOrderItem)).thenReturn(validOrderItem);

        OrderItem result = sut.create(validOrderItem);
        assertEquals(validOrderItem, result);
    }

    @Test
    public void testUpdateOrderItem() throws OrderItemNotFoundException {
        when(mockOrderItemRepository.findById(validOrderItem.getOrderItemId())).thenReturn(Optional.ofNullable(validOrderItem));
        when(mockOrderItemRepository.save(validOrderItem)).thenReturn(validOrderItem);

        OrderItem result = sut.update(validOrderItem);

        assertNotNull(result);
        assertEquals(validOrderItem, result);

        verify(mockOrderItemRepository, times(1)).save(validOrderItem);
        verify(mockOrderItemRepository, times(1)).findById(validOrderItem.getOrderItemId());
    }

    @Test
    public void testDeleteOrderItem() {
        assertTrue(sut.delete(validOrderItem.getOrderItemId()));
        verify(mockOrderItemRepository, times(1)).deleteById(validOrderItem.getOrderItemId());
    }

    @Test
    public void testFindByOrderIdAndProductId() throws Exception{
        when(mockOrderItemRepository.findByOrderIdAndProductId
                (validOrderItem.getOrder().getOrderId(), validOrderItem.getProduct().getProduct_id()))
        .thenReturn(validOrderItem);

        OrderItem result = sut.findByOrderIdAndProductId
                (validOrderItem.getOrder().getOrderId(), validOrderItem.getProduct().getProduct_id());

        assertNotNull(result);
        assertEquals(validOrderItem, result);
    }
}
