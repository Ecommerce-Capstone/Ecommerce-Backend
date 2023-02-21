package com.example.orderservice.infrastructure.data.kafka.publisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Publisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void reduceStock(Long productId, Integer stock){
        stock = -stock;
        StringBuilder sb = new StringBuilder();
        sb.append(productId.toString());
        sb.append(",");
        sb.append(stock.toString());
        kafkaTemplate.send("reduce", sb.toString());
    }

    public void addStock(Long productId, Integer stock){
        StringBuilder sb = new StringBuilder();
        sb.append(productId.toString());
        sb.append(",");
        sb.append(stock.toString());
        kafkaTemplate.send("add", sb.toString());
    }
}
