package com.revature.eeecommerce.OrderItem;

import com.revature.eeecommerce.util.exceptions.OrderItemNotFoundException;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderItemService{
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository){ this.orderItemRepository = orderItemRepository; }

    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    public OrderItem create(OrderItem newOrderItem) {
        return orderItemRepository.save(newOrderItem);
    }

    public OrderItem findById(int orderItemId) throws OrderItemNotFoundException{
        return orderItemRepository.findById(orderItemId).orElseThrow(() -> new OrderItemNotFoundException("Nothing in the database with ID of " + orderItemId));
    }


    public List<OrderItem> findAllByOrderId(int orderId) throws OrderItemNotFoundException {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
        if(orderItems == null) throw new OrderItemNotFoundException("No order item found with that order id and product id");

        return orderItems;
    }
    public OrderItem findByOrderIdAndProductId(int orderId, int productId) throws OrderItemNotFoundException{
        OrderItem orderItem = orderItemRepository.findByOrderIdAndProductId(orderId, productId);
        if(orderItem == null) throw new OrderItemNotFoundException("No order item found with that order id and product id");

        return orderItem;
    }

    public boolean delete(int orderItemId) {
        orderItemRepository.deleteById(orderItemId);
        return true;
    }

    public OrderItem update(OrderItem orderItem) throws OrderItemNotFoundException{
        OrderItem existingOrderitem = orderItemRepository.findById(orderItem.getOrderItemId()).orElseThrow(()-> new OrderItemNotFoundException("No order item found with an id of "+orderItem));
        if(orderItem.getOrder() != null) existingOrderitem.setOrder(orderItem.getOrder());

        if(orderItem.getProduct() != null) existingOrderitem.setProduct(orderItem.getProduct());

        if(orderItem.getCount() > 0) existingOrderitem.setCount(orderItem.getCount());



        return orderItemRepository.save(existingOrderitem);
    }


}
