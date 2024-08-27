package com.revature.eeecommerce.OrderItem;

import com.revature.eeecommerce.Cart.Cart;
import com.revature.eeecommerce.Order.Order;
import com.revature.eeecommerce.Product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Order-Items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(columnDefinition = "integer default 1 check (count > 0)", nullable = false)
    @NotNull
    private int count;

    @Column(columnDefinition = "float(53) default 0 check (discount >= 0)")
    private double discount;

    public OrderItem(Cart cartItem, Order order) {
        this.order = order;
        this.product = cartItem.getProduct();
        this.count = cartItem.getCount();
        this.discount = cartItem.getProduct().getDiscount();
    }

}