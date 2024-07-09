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

    // @PostMapping
    // public Mono<ResponseEntity<Payments>> createPayment(@Valid @RequestBody
    // PaymentsDTO paymentsDTO) {
    // log.info("Creating payment: {}", paymentsDTO);

    // return paymentsService.createPayment(paymentsDTO)
    // .map(payment -> new ResponseEntity<>(payment, HttpStatus.CREATED))
    // .doOnError(error -> log.error("Error creating payment: {}",
    // error.getMessage()));
    // }

    // @GetMapping("/{id}")
    // public Mono<ResponseEntity<Payments>> getPaymentById(@PathVariable @NotNull
    // Long id) {
    // log.info("Retrieving payment by ID: {}", id);

    // return paymentsService.getPaymentById(id)
    // .map(payment -> new ResponseEntity<>(payment, HttpStatus.OK))
    // .doOnError(error -> log.error("Error retrieving payment by ID: {}",
    // error.getMessage()));
    // }

    // @GetMapping
    // public Flux<Payments> getAllPayments() {
    // log.info("Retrieving all payments");

    // return paymentsService.getAllPayments()
    // .doOnError(error -> log.error("Error retrieving all payments: {}",
    // error.getMessage()));
    // }

    // @PutMapping("/{id}")
    // public Mono<ResponseEntity<Payments>> updatePayment(@PathVariable @NotNull
    // Long id,
    // @Valid @RequestBody PaymentsDTO paymentsDTO) {
    // log.info("Updating payment with ID: {}", id);

    // return paymentsService.updatePayment(id, paymentsDTO)
    // .map(payment -> new ResponseEntity<>(payment, HttpStatus.OK))
    // .doOnError(error -> log.error("Error updating payment: {}",
    // error.getMessage()));
    // }

    // @DeleteMapping("/{id}")
    // public Mono<ResponseEntity<Void>> deletePayment(@PathVariable @NotNull Long
    // id) {
    // log.info("Deleting payment with ID: {}", id);

    // return paymentsService.deletePayment(id)
    // .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
    // .doOnError(error -> log.error("Error deleting payment: {}",
    // error.getMessage()));
    // }

}
