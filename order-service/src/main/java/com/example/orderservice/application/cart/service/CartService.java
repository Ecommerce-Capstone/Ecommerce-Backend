package com.example.orderservice.application.cart.service;
import com.example.orderservice.application.cart.entity.Cart;
import com.example.orderservice.application.cart.repository.CartRepository;
import com.example.orderservice.application.cart.usecase.CartUseCase;
import com.example.orderservice.application.product.entity.Product;
import com.example.orderservice.application.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService implements CartUseCase {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    @Override
    public List<Cart> getCarts() {
        List<Cart> carts = cartRepository.findAll();
        return this.addProductToCarts(carts);
    }
    @Override
    public List<Cart> getCarts(Long userId) {
        List<Cart> carts = cartRepository.findAll(userId);
        return this.addProductToCarts(carts);
    }
    private List<Cart> addProductToCarts(List<Cart> carts){
        if (carts.isEmpty()){
            throw new NoSuchElementException();
        }
        try {
            List<Long> productIds = new ArrayList<>();
            HashMap<Long, Integer> productQuantities = new HashMap<>();
            for (Cart cart: carts){
                productQuantities.put(cart.getProductId(), cart.getQuantity());
                productIds.add(cart.getProductId());
            }
            List<Product> products = productRepository.getProducts(productIds);
            for (Cart cart: carts){
                for (Product product: products){
                    if (cart.getProductId().equals(product.getId())){
                        cart.setProduct(product);
                    }
                }
            }
        } catch (HttpClientErrorException httpClientErrorException){
            log.error("Failed when get products {}", httpClientErrorException.getStatusCode());
        } catch (Exception e){
            log.error("Something happen when get products {}", e.getMessage());
        }
        return carts;
    }
    @Override
    public Cart getCartById(Long id) {
        Cart cart = cartRepository.findById(id);
        return this.addProductToCart(cart);
    }
    @Override
    public Cart saveCart(@Valid Cart cart) {
        cartRepository.deleteCartsByUserIdAndProductId(cart.getUserId(), cart.getProductId());
        Cart savedCart = cartRepository.save(cart);
        return this.addProductToCart(savedCart);
    }

    @Override
    public Cart saveCart(Long id, @Valid Cart cartParam) {
        Cart cart = this.getCartById(id);
        cart.setQuantity(cartParam.getQuantity());
        return cartRepository.save(cart);
    }

    private Cart addProductToCart(Cart cart){
        if (cart == null){
            throw new NoSuchElementException();
        }
        try {
            Product product = productRepository.getProduct(cart.getProductId());
            cart.setProduct(product);
        } catch (HttpClientErrorException httpClientErrorException){
            log.error("Failed when get products {}", httpClientErrorException.getStatusCode());
        } catch (Exception e){
            log.error("Something happen when get products {}", e.getMessage());
        }
        return cart;
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
