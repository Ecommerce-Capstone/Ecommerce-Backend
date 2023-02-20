package com.example.orderservice.application.order.service;

import com.example.orderservice.application.cart.entity.Cart;
import com.example.orderservice.application.cart.usecase.CartUseCase;
import com.example.orderservice.application.order.entity.Order;
import com.example.orderservice.application.order.repository.OrderRepository;
import com.example.orderservice.application.order.type.OrderStatus;
import com.example.orderservice.application.order.usecase.OrderUseCase;
import com.example.orderservice.application.orderitem.entity.OrderItem;
import com.example.orderservice.application.orderitem.usecase.OrderItemUseCase;
import com.example.orderservice.application.product.entity.Product;
import com.example.orderservice.application.product.repository.ProductRepository;
import com.example.orderservice.application.user.entity.User;
import com.example.orderservice.application.user.repository.UserRepository;
import com.example.orderservice.event.publisher.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartUseCase cartUseCase;
    private final OrderItemUseCase orderItemUseCase;
    @Autowired
    OrderEventPublisher orderEventPublisher;
    @Override
    public List<Order> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return this.addUserToOrder(orders);
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        List<Order> orderList = this.addUserToOrder(orders.getContent());
        return new PageImpl<>(orderList, pageable, orders.getTotalElements());
    }
    @Override
    public Page<Order> getOrders(Pageable pageable, Long userId) {
        Page<Order> orders = orderRepository.findAll(pageable, userId);
        List<Order> orderList = this.addUserToOrder(orders.getContent());
        return new PageImpl<>(orderList, pageable, orders.getTotalElements());
    }

    private List<Order> addUserToOrder(List<Order> orders){
        try {
            List<Long> userIds = new ArrayList<>();
            for (Order order: orders){
                userIds.add(order.getUserId());
            }
            List<User> users = userRepository.getUsers(userIds);
            for (Order order: orders){
                for (User user: users){
                    if (order.getUserId().equals(user.getId())){
                        order.setUser(user);
                    }
                }
            }
        } catch (HttpClientErrorException httpClientErrorException){
            log.error("Failed to get users {}", httpClientErrorException.getStatusCode());
        } catch (Exception e){
            log.error("Something happen when get users {}", e.getMessage());
        }
        return orders;
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id);
        try {
            User user = userRepository.getUser(order.getUserId());
            order.setUser(user);
        } catch (HttpClientErrorException httpClientErrorException){
            log.error("Failed to get user with id {} : {}", order.getUserId(), httpClientErrorException.getStatusCode());
        }
        catch (Exception e){
            log.error("Something happen when get user with id {} : {}", order.getUserId(), e.getMessage());
        }
        return order;
    }

    @Override
    public Order createOrder(Long userId) throws IOException {
        List<Cart> carts = cartUseCase.getCarts(userId);
        if (carts.isEmpty()){
            log.error("Cart for userId: {} is empty", userId);
            throw new NoSuchElementException();
        }
        List<Long> productIdList = new ArrayList<>();
        HashMap<Long, Integer> quantities = new HashMap<>();
        for (Cart cart: carts){
            productIdList.add(cart.getProductId());
            quantities.put(cart.getProductId(), cart.getQuantity());
        }
        List<Product> products = productRepository.getProducts(productIdList);
        Integer total = 0;
        Order order = this.saveOrder(new Order(userId, OrderStatus.UNPAID));
        List<OrderItem> orderItems = new ArrayList<>();
        for (Product product: products){
            Integer quantity = quantities.get(product.getId());
            if (quantity != null){
                OrderItem orderItem = new OrderItem(order.getId(), product.getId(), quantity, product.getPrice());
                orderItems.add(orderItem);
                total += quantity * product.getPrice();
            }
        }

        orderItemUseCase.saveAll(orderItems);
        order.setTotal(total);
        this.saveOrder(order);
        orderEventPublisher.orderEvent(order);
        cartUseCase.deleteCartByUserId(userId);
        return order;
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public boolean deleteOrderById(Long id) {
        orderRepository.deleteById(id);
        return true;
    }
}
