package com.revature.eeecommerce.Order;

import com.revature.eeecommerce.Product.Product;
import com.revature.eeecommerce.User.User;
import com.revature.eeecommerce.util.exceptions.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.revature.eeecommerce.User.User.userType.CUSTOMER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTestSuite {
    @Mock // indicates Object needing to be mocked
    private OrderRepository mockOrderRepository;

    @InjectMocks // injects our ProductRepository into our ProductService as a mocked object
    private OrderService sut;

    private static Time now = Time.valueOf(LocalTime.now());
    private static User defaultUser = new User(1, "Amsal", "Kassam", "12345 Fake St.", "test@email.com", "Password123", User.userType.EMPLOYEE);
    private static String userJSON = "{\"userId\":1,\"firstName\":\"Amsal\",\"lastName\":\"Kassam\",\"address\":\"12345 Fake St.\",\"email\":\"test@email.com\",\"password\":\"Password123\",\"userType\":\"EMPLOYEE\"}";

    private static Order defaultOrder = new Order(1, defaultUser, now);
    private static String orderJSON = "{\"orderId\":1,\"user\":"+userJSON+",\"date\":\""+now.toString()+"\"}";

    @Test
    public void testFindAll(){
        List<Order> orders = List.of(defaultOrder);
        when(mockOrderRepository.findAll()).thenReturn(orders);

        List<Order> result = sut.findAll();
        assertEquals(1, result.size());
        assertEquals(defaultOrder, result.get(0));
    }

    @Test
    public void testCreate(){
        when(mockOrderRepository.save(defaultOrder)).thenReturn(defaultOrder);

        Order result = sut.create(defaultOrder);
        assertEquals(defaultOrder, result);
    }

    @Test
    public void testfindById(){
        when(mockOrderRepository.findById(1)).thenReturn(Optional.ofNullable(defaultOrder));

        Order result = sut.findById(1);
        assertEquals(defaultOrder, result);
    }

    @Test
    public void testUpdate(){
        when(mockOrderRepository.save(defaultOrder)).thenReturn(defaultOrder);

        assertEquals(true, sut.update(defaultOrder));
    }

}
