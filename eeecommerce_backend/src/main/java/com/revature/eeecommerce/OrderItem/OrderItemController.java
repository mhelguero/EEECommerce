package com.revature.eeecommerce.OrderItem;

import com.revature.eeecommerce.Order.Order;
import com.revature.eeecommerce.OrderItem.dtos.OrderItemDTO;
import com.revature.eeecommerce.Product.Product;
import com.revature.eeecommerce.Product.ProductService;
import com.revature.eeecommerce.util.exceptions.OrderItemNotFoundException;
import com.revature.eeecommerce.util.exceptions.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orderItems")
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final ProductService productService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService, ProductService productService) {
        this.orderItemService = orderItemService;
        this.productService = productService;
    }

    /*
     * Takes in an OrderItem as a json and returns a response with an OrderItemDTO object as the json response body.
     * Requires there to be an entry in the Order table and an entry in the Product table with the same order_id and product_id as in the request body.
     * @param postDTO - OrderItemDTO json object with an existing order_id and product_id
     * @return orderItemDTO - OrderItemDTO with only orderItemId, orderId, product_id and count instead of Order and Product objects in place of the id's
     */

    //DEPRECATED: use OrderController.checkout instead (POST /orders)
//    @PostMapping
//    private ResponseEntity<?> postNewOrderItem(@Valid @RequestBody OrderItemDTO postDTO, @RequestHeader String userType){
//        try {
//            if (!userType.equals("EMPLOYEE")) {
//                throw new UnauthorizedException("You are not logged in as a Seller");
//            }
//
//            Order order = new Order();
//            order.setOrderId(postDTO.getOrderId());
//
//            Product product = new Product();
//            product.setProduct_id(postDTO.getProduct_id());
//
//            OrderItem newOrderItem = new OrderItem();
//            newOrderItem.setOrder(order);
//            newOrderItem.setProduct(product);
//            newOrderItem.setCount(postDTO.getCount());
//
//
//            newOrderItem = orderItemService.create(newOrderItem);
//            OrderItemDTO orderItemDTO = mapToDTO(newOrderItem);
//
//            return ResponseEntity
//                    .status(HttpStatus.CREATED)
//                    .body(orderItemDTO);
//        } catch(UnauthorizedException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }

    /**
     * Takes in an OrderItem object and returns an OrderItemDTO to simplify the response body with only relevant data.
     * @param orderItem - OrderItem object with Order and Product objects
     * @return - OrderItemDTO object containing orderItemId, orderId, product_id, and count
     */
    private OrderItemDTO mapToDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getOrderItemId(),
                orderItem.getOrder().getOrderId(),
                orderItem.getProduct().getProduct_id(),
                orderItem.getCount()
        );
    }

    /**
     * Returns all entries in the order-items table as a List of OrderItemDTO objects, to show only column values instead of
     * each order item's corresponding Order and Product objects in the response body.
     * @return orderItemDTOs - List of each entry in the order-items table as an OrderItemDTO
     */
    @GetMapping
    private ResponseEntity<List<OrderItemDTO>> findAll(){
        List<OrderItem> orderItems = orderItemService.findAll();
        List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderItemDTOs);
    }

    /**
     *  Returns the order-item entry with the corresponding order_item_id as an OrderItemDTO object, or throws an exception
     *  if no entry with that order_item_id found.
     * @param orderItemId - order_item_id of the entry we want to find in the order-items table.
     * @return - returns the found orderItem in OrderItemDTO format, or a 404 status with a message if none found.
     */
    @GetMapping("/{orderItemId}")
    private ResponseEntity<?> getOrderItem(@PathVariable int orderItemId){
        try {
            OrderItem orderItem = orderItemService.findById(orderItemId);
            OrderItemDTO orderItemDTO = mapToDTO(orderItem);
            return ResponseEntity.ok(orderItemDTO);
        } catch (OrderItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/order/{orderId}")
    private ResponseEntity<?> findAllByOrderId(@PathVariable int orderId){
        try{
            List<OrderItem> orderItems = orderItemService.findAllByOrderId(orderId);
            List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderItemDTOs);
        } catch(OrderItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    /**
     * Returns the order-item entry with the corresponding order_id and product_id as an OrderItemDTO object, or throws an exception
     * if no entry with those values were found.
     * @param orderId - order_id of the entry we want to find in the order-items table
     * @param productId - product_id of the entry we want to find in the order-items table
     * @return - returns the found orderItem in OrderItemDTO format, or a 404 status with a message if none found.
     */
    @GetMapping("/{orderId}/{productId}")
    private ResponseEntity<?> findByOrderOrderIdAndProductProductId(@PathVariable int orderId, @PathVariable int productId){
        try{
            OrderItem orderItem = orderItemService.findByOrderIdAndProductId(orderId, productId);
            OrderItemDTO orderItemDTO = mapToDTO(orderItem);
            return ResponseEntity.ok(orderItemDTO);
        } catch(OrderItemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    /**
     * Deletes the order-item entry with the corresponding order_item_id, or throws an exception if none found.
     * @param orderItemId - order_item_id of the entry we want to delete in the order-items table.
     * @param userType - String denoting if the logged-in user is a customer or employee
     * @return - returns nothing if delete successful, or a 404 status with a message if the order-item entry was not found.
     */
    @DeleteMapping("/{orderItemId}")
    private ResponseEntity<?> deleteOrderItem(@PathVariable int orderItemId, @RequestHeader String userType){
        try {
            if (!userType.equals("EMPLOYEE")) {
                throw new UnauthorizedException("You are not logged in as a Seller");
            }

            orderItemService.delete(orderItemId);
            return ResponseEntity.noContent().build();
        } catch(UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /*
     * Updates the order-item entry with the corresponding order_item_id with the values present in the request body and
     * returns the updated orderItem as an OrderItemDTO, or throws an exception if logged-in user is not an employee
     * or the order-item entry was not found.
     * @param patchDTO - OrderItemDTONoId object with optional values for order_id, product_id, and count.
     * @param orderItemId - order_item_id of the entry we want to update.
     * @param userType - String indicating if the logged-in user is a customer or employee.
     * @return - returns the updated order-item entry in OrderItemDTO format if patch successful, a or message if unsuccessful.
     * @throws UnauthorizedException - thrown if userType is not 'EMPLOYEE'.
     * @throws OrderItemNotFoundException - thrown if no order-item entry found with the corresponding order_item_id.
     */
//    @PatchMapping("/{orderItemId}")
//    private ResponseEntity<?> patchUpdateOrderItem(@Valid @RequestBody OrderItemDTO patchDTO, @PathVariable int orderItemId, @RequestHeader String userType) throws UnauthorizedException {
//        // replacing values in the existing orderItem with any values included in the PATCH request
//        try {
//            if (!userType.equals("EMPLOYEE")) {
//                throw new UnauthorizedException("You are not logged in as a Seller");
//            }
//
//            OrderItem existingOrderItem = orderItemService.findById(orderItemId);
//            if(patchDTO.getOrderId() != null) {
//                Order order = new Order();
//                order.setOrderId(patchDTO.getOrderId());
//                existingOrderItem.setOrder(order);
//            }
//
//            if(patchDTO.getProduct_id() != null) {
//                Product product = new Product();
//                product.setProduct_id(patchDTO.getProduct_id());
//                existingOrderItem.setProduct(product);
//            }
//
//            if(patchDTO.getCount() != null) {
//                existingOrderItem.setCount(patchDTO.getCount());
//            }
//
//            OrderItem updatedOrderItem = orderItemService.update(existingOrderItem);
//
//            // formatting updated orderItem into an orderItemDTO to only include orderId and product_id instead of an Order and Product
//            OrderItemDTO orderItemDTO = mapToDTO(updatedOrderItem);
//
//            return ResponseEntity.ok(orderItemDTO);
//        } catch(OrderItemNotFoundException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch(UnauthorizedException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }


}
