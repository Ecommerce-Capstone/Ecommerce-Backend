package com.example.orderservice.application.cart.repository;

import com.example.orderservice.application.cart.entity.Cart;

import java.util.List;
public interface CartRepository {
    List<Cart> findAll();
    List<Cart> findAll(Long userId);
    Cart findById(Long id);
    Cart save(Cart cart);
    boolean deleteById(Long id);

    void deleteCartsByUserId(Long userId);
    void deleteCartsByUserIdAndProductId(Long userId, Long productId);
}
