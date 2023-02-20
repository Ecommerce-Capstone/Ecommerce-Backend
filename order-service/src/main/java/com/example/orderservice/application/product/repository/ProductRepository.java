package com.example.orderservice.application.product.repository;

import com.example.orderservice.application.product.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface ProductRepository {
    List<Product> getProducts(List<Long> id) throws IOException;
    Product getProduct(Long id);
}
