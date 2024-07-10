package com.bootcamp.orchestrator.service;

import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bootcamp.dto.OrderDTO;
import com.bootcamp.enums.OrderStatus;
import com.bootcamp.enums.PaymentStatus;
import com.bootcamp.enums.ProductStatus;
import com.bootcamp.orchestrator.config.KafkaProducer;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrchestratorService {

    private KafkaProducer kafkaProducer;
    private WebClient webClient;

    private static final String baseProductsUrl = "http://127.0.0.1:8001/api/products";
    private static final String basePaymentUrl = "http://127.0.0.1:8002/api/payments";

    public OrchestratorService(KafkaProducer kafkaProducer, WebClient webClient) {
        this.kafkaProducer = kafkaProducer;
        this.webClient = webClient;
    }

    @KafkaListener(topics = "order-create", groupId = "final-group")
    public void orchestrate(OrderDTO orderDTO) {
        log.info("ORCHESTRATOR: " + orderDTO);
        var test = orderDTO;
        webClient.post()
                .uri(baseProductsUrl + "/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(test)
                .retrieve()
                .bodyToMono(OrderDTO.class)
                .onErrorResume(err -> {
                    orderDTO.setOrder_status(OrderStatus.FAILED.name());
                    kafkaProducer.sendMessage("order-response", orderDTO);
                    return Mono.empty();
                })
                .doOnSuccess(product -> {
                    if (product == null) {
                        orderDTO.setOrder_status(OrderStatus.FAILED.name());
                        kafkaProducer.sendMessage("order-response", orderDTO);
                        return;
                    }
                    if (product.getOrder_status().equals(ProductStatus.OUTOFSTOCK.name())) {
                        product.setOrder_status(OrderStatus.FAILED.name());
                        kafkaProducer.sendMessage("order-response", product);
                        return;
                    }
                    webClient.post()
                            .uri(basePaymentUrl + "/transaction")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .bodyValue(product)
                            .retrieve()
                            .bodyToMono(OrderDTO.class)
                            .onErrorResume(err -> {
                                log.error("PAYMENTS NOT CONNECTED : " + err);
                                orderDTO.setOrder_status(OrderStatus.FAILED.name());
                                kafkaProducer.sendMessage("order-response", orderDTO);
                                webClient.post()
                                        .uri(baseProductsUrl + "/add")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .bodyValue(orderDTO)
                                        .retrieve()
                                        .bodyToMono(OrderDTO.class)
                                        .subscribe();
                                return Mono.empty();
                            }).doOnSuccess(payment -> {
                                if (payment == null) {
                                    orderDTO.setOrder_status(OrderStatus.FAILED.name());
                                    kafkaProducer.sendMessage("order-response", orderDTO);
                                    webClient.post()
                                            .uri(baseProductsUrl + "/add")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON)
                                            .bodyValue(orderDTO)
                                            .retrieve()
                                            .bodyToMono(OrderDTO.class)
                                            .subscribe();
                                    return;
                                }
                                if (payment.getOrder_status().equals(PaymentStatus.REJECTED.name())) {
                                    payment.setOrder_status(OrderStatus.FAILED.name());
                                    webClient.post()
                                            .uri(baseProductsUrl + "/add")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON)
                                            .bodyValue(orderDTO)
                                            .retrieve()
                                            .bodyToMono(OrderDTO.class)
                                            .subscribe();
                                } else
                                    payment.setOrder_status(OrderStatus.COMPLETED.name());
                                kafkaProducer.sendMessage("order-response", payment);
                            }).subscribe();
                })
                .subscribe();
    }

}
