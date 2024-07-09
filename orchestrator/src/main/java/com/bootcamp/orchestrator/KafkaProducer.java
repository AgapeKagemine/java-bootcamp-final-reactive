package com.bootcamp.orchestrator;

import com.bootcamp.dto.OrderDTO;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, OrderDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, OrderDTO msg) {
        kafkaTemplate.send(topic, msg);
    }

}
