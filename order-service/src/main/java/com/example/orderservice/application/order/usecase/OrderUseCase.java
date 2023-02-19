package com.example.orderservice.application.order.usecase;
import com.example.orderservice.application.order.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;

public interface OrderUseCase {
    List<Order> getOrders();
    Page<Order> getOrders(Pageable pageable);
    Page<Order> getOrders(Pageable pageable, Long userId);
    Order getOrderById(Long id);
    Order createOrder(Long userId) throws IOException;
    Order saveOrder(Order order);
    boolean deleteOrderById(Long id);
}
