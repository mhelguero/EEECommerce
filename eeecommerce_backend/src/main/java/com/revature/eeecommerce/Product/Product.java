package com.revature.eeecommerce.Product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** MODEL CLASS DOCUMENTATION
 * @author Arjun Ramsinghani
 * The model class defines our data and what it should contain as well as implement the database via the Spring Boot annotations.
 * All of our products that are created and sent to the database will have all attributes from this class.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;
    @Positive
    @Column(nullable = false)
    private int price;
    @PositiveOrZero
    private double discount;
    @Column(nullable = false)
    private String name;
    private String description;
    @Positive
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int quantity;
    @Column(nullable = false)
    private String image;
}