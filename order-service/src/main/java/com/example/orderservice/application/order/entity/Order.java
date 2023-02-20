package com.example.orderservice.application.order.entity;
import com.example.orderservice.application.order.type.OrderStatus;
import com.example.orderservice.application.user.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private User user;
    private OrderStatus status;
    private Integer total;
    private String paymentUrl;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public Order(Long userId, OrderStatus orderStatus){
        this.userId = userId;
        this.status = orderStatus;
    }
}

