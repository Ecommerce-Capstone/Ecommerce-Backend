package com.example.orderservice.application.cart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCart {
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private String description;
    private String images;
    private Long categoryId;
    private Integer quantity;
}
