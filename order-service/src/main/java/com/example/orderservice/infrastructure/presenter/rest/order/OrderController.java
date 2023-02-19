package com.example.orderservice.infrastructure.presenter.rest.order;

import com.example.orderservice.application.order.entity.Order;
import com.example.orderservice.application.order.service.OrderService;
import com.example.orderservice.application.order.type.OrderStatus;
import com.example.orderservice.application.order.usecase.OrderUseCase;
import com.example.orderservice.application.orderitem.entity.OrderItem;
import com.example.orderservice.application.orderitem.usecase.OrderItemUseCase;
import com.example.orderservice.event.publisher.OrderEventPublisher;
import com.example.orderservice.infrastructure.presenter.rest.Response;
import com.example.orderservice.infrastructure.presenter.rest.authentication.dto.AuthenticatedUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderUseCase orderUseCase;
    private final OrderItemUseCase orderItemUseCase;
    @GetMapping
    public ResponseEntity<Object> getOrder(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId
    ){
        log.info("GET /order called");
        Response response = new Response();
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = userId == null ? orderUseCase.getOrders(pageable) : orderUseCase.getOrders(pageable, userId);
        response.setPageData(orders);
        return response.getResponse();
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id){
        log.info("GET /order/{} called", id);
        Response response = new Response();
        Order order = orderUseCase.getOrderById(id);
        response.setData(order);
        return response.getResponse();
    }
    @GetMapping(path = "/{id}/items")
    public ResponseEntity<Object> getOrderItemsByOrderId(@PathVariable Long id){
        log.info("GET /order/{}/items called", id);
        Response response = new Response();
        List<OrderItem> orderItems = orderItemUseCase.getOrderItemByOrderId(id);
        response.setData(orderItems);
        return response.getResponse();
    }
    @PostMapping
    public ResponseEntity<Object> createOrder() throws IOException {
        log.info("POST /order called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        log.debug(authenticatedUser.getUserId().toString());
        Response response = new Response();
        Order order = orderUseCase.createOrder(authenticatedUser.getUserId());
        response.setData(order);
        response.setHttpCode(201);
        return response.getResponse();
    }
    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> saveOrder(@PathVariable Long id, @RequestBody Order order){
        log.info("GET /order/{id} called");
        Response response = new Response();

        return response.getResponse();
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteOrderById(@PathVariable Long id){
        log.info("GET /order/{id} called");
        Response response = new Response();
        orderUseCase.deleteOrderById(id);
        response.setHttpCode(204);
        return response.getResponse();
    }
}
