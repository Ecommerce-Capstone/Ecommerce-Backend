package com.example.orderservice.application.cart.entity;
import com.example.orderservice.application.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Product product;
    private Long userId;
    private Integer quantity;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

}