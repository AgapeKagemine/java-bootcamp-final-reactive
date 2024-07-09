package com.bootcamp.payment.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.bootcamp.dto.OrderDTO;
import com.bootcamp.enums.PaymentStatus;
import com.bootcamp.payment.model.Balance;
import com.bootcamp.payment.model.Payments;
import com.bootcamp.payment.repository.BalanceRepository;
import com.bootcamp.payment.repository.PaymentsRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Validated
@Slf4j
public class PaymentsService {

    private PaymentsRepository paymentsRepository;
    private BalanceRepository balanceRepository;

    public PaymentsService(PaymentsRepository paymentsRepository, BalanceRepository balanceRepository) {
        this.paymentsRepository = paymentsRepository;
        this.balanceRepository = balanceRepository;
    }

    public Flux<Payments> findAllTransaction() {
        log.info("Fetching all transaction");
        return paymentsRepository.findAll();
    }

    public Flux<Balance> findAllBalance() {
        log.info("Fecthing all balance");
        return balanceRepository.findAll();
    }

    @Transactional
    public Mono<OrderDTO> transaction(OrderDTO request) {
        log.info("Processing transactions: " + request);
        return balanceRepository.findById(request.getCustomer_id()).flatMap(balance -> {
            Payments payments = new Payments();
            if (request.getTotal_amount() > balance.getAmount()) {
                request.setOrder_status(PaymentStatus.REJECTED.name());

                payments.setAmount(request.getTotal_amount());
                payments.setStatus(PaymentStatus.REJECTED.toString());
            } else {
                balance.setAmount(balance.getAmount() - request.getTotal_amount());
                balanceRepository.save(balance).subscribe();

                request.setOrder_status(PaymentStatus.APPROVED.name());
                payments.setAmount(request.getTotal_amount());
                payments.setStatus(PaymentStatus.APPROVED.toString());
            }
            payments.setOrder_id(request.getId());
            payments.setPayment_date(LocalDate.now());
            payments.setMode(request.getPayment_method());
            payments.setReference_number(UUID.randomUUID().toString());
            log.info("PAYMENTS " + payments);
            paymentsRepository.save(payments).subscribe();
            return Mono.just(request);
        });
    }

    // public Mono<Payments> createPayment(@Valid PaymentsDTO paymentDTO) {
    // Payments payment = mapToEntity(paymentDTO);
    // return paymentsRepository.save(payment);
    // // .doOnSuccess(savedPayment -> log.info("Payment created: {}",
    // savedPayment))
    // // .doOnError(error -> log.error("Error creating payment: {}",
    // // error.getMessage()))
    // // .onErrorMap(e -> {
    // // log.error("Error fetching all products: {}", e.getMessage());
    // // return new PaymentsException("Failed to fetch all products");
    // // });
    // }

    // public Mono<Payments> getPaymentById(@NotNull Long id) {
    // return paymentsRepository.findById(id);
    // // .doOnNext(payment -> log.info("Payment retrieved: {}", payment))
    // // .doOnError(error -> log.error("Error retrieving payment by ID: {}",
    // // error.getMessage()))
    // // .onErrorMap(e -> {
    // // log.error("Error fetching all products: {}", e.getMessage());
    // // return new PaymentsException("Failed to fetch all products");
    // // })
    // // .switchIfEmpty(Mono.error(new RuntimeException("Payment not found")));
    // }

    // public Flux<Payments> getAllPayments() {
    // return paymentsRepository.findAll();
    // // .doOnComplete(() -> log.info("All payments retrieved"))
    // // .doOnError(error -> log.error("Error retrieving all payments: {}",
    // // error.getMessage()))
    // // .onErrorMap(e -> {
    // // log.error("Error fetching all products: {}", e.getMessage());
    // // return new PaymentsException("Failed to fetch all products");
    // // });
    // }

    // public Mono<Payments> updatePayment(@NotNull Long id, @Valid PaymentsDTO
    // paymentDTO) {
    // return paymentsRepository.findById(id)
    // .flatMap(existingPayment -> {
    // existingPayment = mapToEntity(paymentDTO);
    // existingPayment.setId(id);
    // existingPayment.setPayment_date(LocalDate.now());
    // existingPayment.setReference_number(java.util.UUID.randomUUID().toString());
    // return paymentsRepository.save(existingPayment)
    // .doOnSuccess(updatedPayment -> log.info("Payment updated: {}",
    // updatedPayment))
    // .doOnError(error -> log.error("Error updating payment: {}",
    // error.getMessage()));
    // });
    // // .switchIfEmpty(Mono.error(new RuntimeException("Payment not found")))
    // // .onErrorMap(e -> {
    // // log.error("Error fetching all products: {}", e.getMessage());
    // // return new PaymentsException("Failed to fetch all products");
    // // });
    // }

    // public Mono<Void> deletePayment(@NotNull Long id) {
    // return paymentsRepository.deleteById(id);
    // // .doOnSuccess(unused -> log.info("Payment deleted with ID: {}", id))
    // // .doOnError(error -> log.error("Error deleting payment: {}",
    // // error.getMessage()))
    // // .onErrorMap(e -> {
    // // log.error("Error fetching all products: {}", e.getMessage());
    // // return new PaymentsException("Failed to fetch all products");
    // // });
    // }

    // public Payments mapToEntity(PaymentsDTO paymentDTO) {
    // var payment = new Payments();
    // payment.setAmount(paymentDTO.getAmount());
    // payment.setMode(paymentDTO.getMode());
    // payment.setOrder_id(paymentDTO.getOrder_id());
    // payment.setPayment_date(paymentDTO.getPayment_date());
    // payment.setReference_number(paymentDTO.getReference_number());
    // payment.setStatus(paymentDTO.getStatus());
    // return payment;
    // }
}
