package com.revature.eeecommerce.Order;

import com.revature.eeecommerce.util.exceptions.DataNotFoundException;
import com.revature.eeecommerce.util.interfaces.Serviceable;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements Serviceable<Order> {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public List<Order> findAll() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()){
            throw new DataNotFoundException("No order information available");
        } else {
            return orders;
        }
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findById(int orderNumber) {
        Order foundOrder = orderRepository.findById(orderNumber)
                .orElseThrow(() -> new DataNotFoundException("No order with the ID of " + orderNumber));
        return foundOrder;
    }

    @Transactional
    @Override
    public boolean update(Order orderToUpdate) {
        orderRepository.save(orderToUpdate);
        return true;
    }

    @Override
    public boolean delete(Order order) {
        orderRepository.delete(order);
        return true;
    }

    public List<Order> findAllById(int userId) {
        List<Order> orders = orderRepository.findAllByUserUserId(userId);
        if (orders.isEmpty()){
            throw new DataNotFoundException("No orders with that userId was found");
        } else {
            return orders;
        }
    }
}
