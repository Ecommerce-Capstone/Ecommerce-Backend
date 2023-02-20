package com.example.orderservice.application.cart.service;
import com.example.orderservice.application.cart.entity.Cart;
import com.example.orderservice.application.cart.entity.ProductCart;
import com.example.orderservice.application.cart.repository.CartRepository;
import com.example.orderservice.application.cart.usecase.CartUseCase;
import com.example.orderservice.application.product.entity.Product;
import com.example.orderservice.application.product.repository.ProductRepository;
import com.example.orderservice.infrastructure.data.jpa.cart.CartEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor

public class CartService implements CartUseCase {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    @Override
    public List<Cart> getCarts() {
        return cartRepository.findAll();
    }
    @Override
    public List<Cart> getCarts(Long userId) {
        return cartRepository.findAll(userId);
    }

    @Override
    public List<ProductCart> getProductCarts(Long userId) throws IOException {
        List<Cart> carts = this.getCarts(userId);
        if (carts.isEmpty()){
            throw new NoSuchElementException();
        }
        List<Long> productIds = new ArrayList<>();
        HashMap<Long, Integer> productQuantities = new HashMap<>();
        for (Cart cart: carts){
            productQuantities.put(cart.getProductId(), cart.getQuantity());
            productIds.add(cart.getProductId());
        }
        List<Product> products = productRepository.getProducts(productIds);
        List<ProductCart> productCarts = new ArrayList<>();
        for (Product product: products){
            ProductCart productCart = new ProductCart(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock(),
                    product.getDescription(),
                    product.getImages(),
                    product.getCategoryId(),
                    productQuantities.get(product.getId())
            );
            productCarts.add(productCart);
        }
        return productCarts;
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id);
    }
    @Override
    public Cart saveCart(@Valid Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart saveCart(Long id, @Valid Cart cartParam) {
        Cart cart = this.getCartById(id);
        cart.setQuantity(cartParam.getQuantity());
        return cartRepository.save(cart);
    }

    @Override
    public boolean deleteCartById(Long id) {
        cartRepository.deleteById(id);
        return true;
    }

    @Override
    public void deleteCartByUserId(Long userId) {
        cartRepository.deleteCartsByUserId(userId);
    }
}
