package com.example.productservice.infrastructure.data.kafka.subsciber;

import com.example.productservice.application.products.entity.Product;
import com.example.productservice.application.products.usecase.ProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Subscriber {
    private final ProductUseCase productUseCase;
    @KafkaListener(topics = "reduce", concurrency = "5")
    public void reduceStockSubscriber(@Payload String message){
        log.info("Received reduce message: " + message);
        String[] split = message.split(",");
        Long productId = Long.valueOf(split[0]);
        Integer quantity = Integer.valueOf(split[1]);
        log.info("product id: {} qty: {}", productId, quantity);
        Product product = productUseCase.getProductById(productId);
        System.out.println(product);
        product.setStock(product.getStock() + quantity);
        product.setSold(product.getSold() + Math.abs(quantity));
        productUseCase.saveProduct(product);
    }

    @KafkaListener(topics = "add", concurrency = "5")
    public void addStockSubscriber(@Payload String message){
        log.info("Received add message: " + message);
        String[] split = message.split(",");
        Long productId = Long.valueOf(split[0]);
        Integer quantity = Integer.valueOf(split[1]);

        Product product = productUseCase.getProductById(productId);
        product.setStock(product.getStock() + quantity);
        quantity = -quantity;
        product.setSold(product.getSold() + quantity);
        productUseCase.saveProduct(product);
    }
}
