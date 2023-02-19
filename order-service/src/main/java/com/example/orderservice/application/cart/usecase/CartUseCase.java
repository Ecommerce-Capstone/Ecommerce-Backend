package com.example.orderservice.application.cart.usecase;
import com.example.orderservice.application.cart.entity.Cart;

import java.util.List;

public interface CartUseCase {
    List<Cart> getCarts();
    List<Cart> getCarts(Long userId);
    Cart getCartById(Long id);
    Cart saveCart(Cart cart);
    Cart saveCart(Long id, Cart cart);
    boolean deleteCartById(Long id);
    void deleteCartByUserId(Long userId);
}
