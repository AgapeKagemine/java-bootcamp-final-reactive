package com.bootcamp.orders.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.bootcamp.dto.OrderDTO;

@Component
public class KafkaProducer {

    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, OrderDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, OrderDTO orderDTO) {
        kafkaTemplate.send(topic, orderDTO);
    }

}
