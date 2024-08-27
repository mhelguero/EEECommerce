package com.revature.eeecommerce.Order;

import com.revature.eeecommerce.Cart.Cart;
import com.revature.eeecommerce.Cart.CartService;
import com.revature.eeecommerce.OrderItem.OrderItem;
import com.revature.eeecommerce.OrderItem.OrderItemService;
import com.revature.eeecommerce.Product.Product;
import com.revature.eeecommerce.Product.ProductService;
import com.revature.eeecommerce.User.UserService;
import com.revature.eeecommerce.util.exceptions.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, OrderItemService orderItemService, UserService userService, ProductService productService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public @ResponseBody List<Order> getAllOrders(){ return orderService.findAll(); }

    @PostMapping
    public ResponseEntity<List<OrderItem>> checkout(@RequestHeader int userId){
        List<Cart> totalCart = cartService.findCartByUserId(userId);
        Order newOrder = new Order();
        newOrder.setUser(userService.findById(userId));
        newOrder.setDate(Time.valueOf(LocalTime.now()));
        newOrder = orderService.create(newOrder);

        Order finalNewOrder = newOrder; // copying variable to make java happy with using in lambda expr
        List<OrderItem> orderedItems = new ArrayList<>();
        totalCart.forEach((cartItem) -> {
            OrderItem newOrderItem = new OrderItem(cartItem, finalNewOrder);
            Product product = newOrderItem.getProduct();
            if(product.getQuantity() > newOrderItem.getCount()){
                product.setQuantity(product.getQuantity() - newOrderItem.getCount());
                productService.updateProduct(product);
                orderItemService.create(newOrderItem);
                cartService.deleteCart(cartItem.getCartId());
                orderedItems.add(newOrderItem);
            }

        });
        return ResponseEntity.status(HttpStatus.CREATED).body(orderedItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PutMapping
    public ResponseEntity<Boolean> putUpdateOrder(@Valid @RequestBody Order updatedOrder) {
        return ResponseEntity.ok(orderService.update(updatedOrder));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getAllOrdersByUserId(@PathVariable int userId){
        return ResponseEntity.ok(orderService.findAllById(userId));
    }
}