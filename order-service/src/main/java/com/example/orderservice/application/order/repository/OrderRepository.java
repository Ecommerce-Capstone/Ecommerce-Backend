package com.example.orderservice.application.order.repository;

import com.example.orderservice.application.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepository {
    List<Order> findAll();
    Page<Order> findAll(Pageable pageable);
    Page<Order> findAll(Pageable pageable, Long userId);
    Order findById(Long id);
    Order save(Order order);
    boolean deleteById(Long id);
}
