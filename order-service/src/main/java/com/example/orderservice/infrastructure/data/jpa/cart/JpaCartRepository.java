package com.example.orderservice.infrastructure.data.jpa.cart;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JpaCartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findAllByUserId(Long userId);
    void deleteByUserId(Long userId);
    @Transactional
    void deleteByUserIdAndProductId(Long userId, Long productId);
}
