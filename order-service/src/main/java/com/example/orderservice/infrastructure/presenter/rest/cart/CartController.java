package com.example.orderservice.infrastructure.presenter.rest.cart;

import com.example.orderservice.application.cart.entity.Cart;
import com.example.orderservice.application.cart.entity.ProductCart;
import com.example.orderservice.application.cart.usecase.CartUseCase;
import com.example.orderservice.infrastructure.presenter.rest.Response;
import com.example.orderservice.infrastructure.presenter.rest.authentication.dto.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartUseCase cartUseCase;
    @GetMapping
    public ResponseEntity<Object> getCarts(@RequestParam(required = false) Long userId) throws IOException {
        log.info("GET /cart called");
        Response response = new Response();
        if (userId == null){
            List<Cart> carts = cartUseCase.getCarts();
            response.setData(carts);
        } else {
            List<ProductCart> carts = cartUseCase.getProductCarts(userId);
            response.setData(carts);
        }
        return response.getResponse();
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getCartById(@PathVariable Long id){
        log.info("GET /cart/{} called", id);
        Response response = new Response();
        Cart cart = cartUseCase.getCartById(id);
        response.setData(cart);
        return response.getResponse();
    }
    @PostMapping
    public ResponseEntity<Object> createCart(@Valid @RequestBody Cart cart){
        log.info("POST /cart called with body: {}", cart);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        cart.setUserId(authenticatedUser.getUserId());

        Response response = new Response();
        cart = cartUseCase.saveCart(cart);
        response.setData(cart);
        response.setHttpCode(201);
        return response.getResponse();
    }
    @PatchMapping(path = "/{id}")
    public ResponseEntity<Object> updateCart(@PathVariable Long id, @RequestBody Cart cart){
        log.info("PATCH /cart/{} called with body: {}", id, cart);
        Response response = new Response();
        cart = cartUseCase.saveCart(id, cart);
        response.setData(cart);
        return response.getResponse();
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteCart(@PathVariable Long id){
        log.info("DELETE /cart/{} called", id);
        Response response = new Response();
        cartUseCase.deleteCartById(id);
        response.setHttpCode(204);
        return response.getResponse();
    }
}
