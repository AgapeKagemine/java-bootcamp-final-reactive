package com.bootcamp.orchestrator.service;

import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bootcamp.dto.OrderDTO;
import com.bootcamp.enums.OrderStatus;
import com.bootcamp.enums.PaymentStatus;
import com.bootcamp.enums.ProductStatus;
import com.bootcamp.orchestrator.KafkaProducer;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrchestratorService {

    private KafkaProducer kafkaProducer;
    private WebClient webClient;

    private static final String baseProductsUrl = "http://127.0.0.1:8001/api/products";
    private static final String basePaymentUrl = "http://127.0.0.1:8004/api/payments";

    public OrchestratorService(KafkaProducer kafkaProducer, WebClient webClient) {
        this.kafkaProducer = kafkaProducer;
        this.webClient = webClient;
    }

    @KafkaListener(topics = "order-create", groupId = "final-group")
    public void orchestrate(OrderDTO orderDTO) {
        log.info("ORCHESTRATOR: " + orderDTO);
        webClient.post()
                .uri(baseProductsUrl + "/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(orderDTO)
                .retrieve()
                .bodyToMono(OrderDTO.class)
                .doOnError(err -> {
                    orderDTO.setOrder_status(OrderStatus.FAILED.name());
                    kafkaProducer.sendMessage("order-response", orderDTO);
                    Mono.error(new RuntimeException("Unreachable! " + err));
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
                            .doOnError(err -> {
                                orderDTO.setOrder_status(OrderStatus.FAILED.name());
                                kafkaProducer.sendMessage("order-response", orderDTO);
                                Mono.error(new RuntimeException("Unreachable! " + err));
                            }).doOnSuccess(payment -> {
                                if (payment == null) {
                                    orderDTO.setOrder_status(OrderStatus.FAILED.name());
                                    kafkaProducer.sendMessage("order-response", orderDTO);
                                    return;
                                }
                                if (payment.getOrder_status().equals(PaymentStatus.REJECTED.name())) {
                                    payment.setOrder_status(OrderStatus.FAILED.name());
                                    var test = webClient.post()
                                            .uri(baseProductsUrl + "/add")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON)
                                            .bodyValue(orderDTO)
                                            .retrieve()
                                            .bodyToMono(OrderDTO.class)
                                            .subscribe();
                                    log.info("Rollback? " + test);
                                } else {
                                    payment.setOrder_status(OrderStatus.COMPLETED.name());
                                }
                                kafkaProducer.sendMessage("order-response", payment);
                                return;
                            }).subscribe();
                })
                .subscribe();
    }

    // public Mono<OrdersRequest> create() {
    // return webClient.get().uri(baseOrderUrl +
    // "/create").retrieve().bodyToMono(OrdersRequest.class);
    // }

}
