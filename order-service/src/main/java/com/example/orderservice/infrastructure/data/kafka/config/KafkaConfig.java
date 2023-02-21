package com.example.orderservice.infrastructure.data.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public NewTopic addTopic() {
        return new NewTopic("add", 2, (short) 1);
    }
    @Bean
    public NewTopic reduceTopic() {
        return new NewTopic("reduce", 2, (short) 1);
    }
}
