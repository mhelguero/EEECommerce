package com.revature.eeecommerce.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findAllByOrderId(@Param("orderId") int orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId AND oi.product.product_id = :productId")
    OrderItem findByOrderIdAndProductId(@Param("orderId") int orderId,@Param("productId") int productId);
}
