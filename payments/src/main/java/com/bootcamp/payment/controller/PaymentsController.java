package com.bootcamp.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.dto.OrderDTO;
import com.bootcamp.payment.model.Balance;
import com.bootcamp.payment.model.Payments;
import com.bootcamp.payment.service.PaymentsService;

import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/payments")
@Validated
public class PaymentsController {

    private PaymentsService paymentsService;

    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @PostMapping("/transaction")
    public Mono<ResponseEntity<OrderDTO>> transaction(@RequestBody OrderDTO orderDTO) {
        return paymentsService.transaction(orderDTO).map(ResponseEntity::ok);
    }

    @GetMapping("/transaction")
    public Flux<Payments> getAllTransaction() {
        return paymentsService.findAllTransaction();
    }

    @GetMapping("/balance")
    public Flux<Balance> getAllBalance() {
        return paymentsService.findAllBalance();
    }

}
