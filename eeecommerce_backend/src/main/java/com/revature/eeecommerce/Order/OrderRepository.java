package com.revature.eeecommerce.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Order repository follows the Data Access Object (DAO) pattern
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserUserId(int userId);
}
