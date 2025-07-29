package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private Integer quantity;
    private Double price;
}
