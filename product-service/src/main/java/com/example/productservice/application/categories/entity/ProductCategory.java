package com.example.productservice.application.categories.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    private Long id;
    @NotEmpty(message = "Product category name is required")
    private String name;
    private String description;
    private String image;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
