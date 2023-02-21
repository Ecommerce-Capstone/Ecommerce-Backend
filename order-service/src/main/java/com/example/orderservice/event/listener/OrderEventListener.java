package com.example.orderservice.event.listener;

import com.example.orderservice.application.order.entity.Order;
import com.example.orderservice.application.order.type.OrderStatus;
import com.example.orderservice.application.order.usecase.OrderUseCase;
import com.example.orderservice.application.orderitem.entity.OrderItem;
import com.example.orderservice.application.orderitem.usecase.OrderItemUseCase;
import com.example.orderservice.infrastructure.data.kafka.publisher.Publisher;
import com.example.orderservice.infrastructure.data.rest.xendit.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {
    private final OrderUseCase orderUseCase;
    private final OrderItemUseCase orderItemUseCase;
    @Autowired
    private Publisher publisher;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Async
    @EventListener(condition = "@orderPredicate.orderUnpaid(#order.status)")
    void orderCreated(Order order) {
        log.info("Received order UNPAID event");
        List<OrderItem> orderItems = orderItemUseCase.getOrderItemByOrderId(order.getId());
        for (OrderItem orderItem: orderItems){
            publisher.reduceStock(orderItem.getProductId(), orderItem.getQuantity());
        }
        simpMessagingTemplate.convertAndSend("/i/order-" + order.getId().toString(), order);
    }

    @Async
    @EventListener(condition = "@orderPredicate.orderPaid(#order.status)")
    void orderPaid(Order order) {
        log.info("Received order PAID event");
        order.setStatus(OrderStatus.PAID);
        orderUseCase.saveOrder(order);
        simpMessagingTemplate.convertAndSend("/i/order-" + order.getId().toString(), order);
    }

    @Async
    @EventListener(condition = "@orderPredicate.orderExpired(#order.status)")
    void orderExpired(Order order) {
        log.info("Received order EXPIRED event");
        order.setStatus(OrderStatus.EXPIRED);
        orderUseCase.saveOrder(order);
        simpMessagingTemplate.convertAndSend("/i/order-" + order.getId().toString(), order);
    }

}
