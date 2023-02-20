package com.example.orderservice.application.orderitem.entity;

import com.example.orderservice.application.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImages;
    private Integer quantity;
    private Integer price;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}

