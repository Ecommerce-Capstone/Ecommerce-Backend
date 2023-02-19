package com.example.orderservice.infrastructure.data.jpa.order;

import com.example.orderservice.application.order.entity.Order;
import com.example.orderservice.application.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public List<Order> findAll() {
        List<OrderEntity> orderEntities = (List<OrderEntity>) jpaOrderRepository.findAll();
        return this.castOrderEntityToOrder(orderEntities);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        Page<OrderEntity> orderEntities = jpaOrderRepository.findAll(pageable);
        return this.castOrderEntityToOrder(orderEntities, pageable);
    }

    @Override
    public Page<Order> findAll(Pageable pageable, Long userId) {
        Page<OrderEntity> orderEntities = jpaOrderRepository.findAllByUserId(userId, pageable);
        return this.castOrderEntityToOrder(orderEntities, pageable);
    }

    @Override
    public Order findById(Long id) {
        Optional<OrderEntity> orderEntities = jpaOrderRepository.findById(id);
        if (orderEntities.isEmpty()){
            throw new NoSuchElementException();
        }
        Order order = new Order();
        BeanUtils.copyProperties(orderEntities.get(), order);
        return order;
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntities = new OrderEntity();
        BeanUtils.copyProperties(order, orderEntities);
        orderEntities = jpaOrderRepository.save(orderEntities);
        BeanUtils.copyProperties(orderEntities, order);
        return order;
    }

    @Override
    public boolean deleteById(Long id) {
        jpaOrderRepository.deleteById(id);
        return true;
    }

    private List<Order> castOrderEntityToOrder(List<OrderEntity> orderEntities){
        List<Order> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities){
            Order order = new Order();
            BeanUtils.copyProperties(orderEntity, order);
            orders.add(order);
        }
        return orders;
    }

    private Page<Order> castOrderEntityToOrder(Page<OrderEntity> orderEntities, Pageable pageable){
        List<Order> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities.getContent()){
            Order order = new Order();
            BeanUtils.copyProperties(orderEntity, order);
            orders.add(order);
        }
        return new PageImpl<>(orders, pageable, orderEntities.getTotalElements());
    }

}
