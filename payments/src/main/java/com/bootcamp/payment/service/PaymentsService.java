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
            paymentsRepository.save(payments).subscribe();
            return Mono.just(request);
        }).switchIfEmpty(Mono.defer(() -> {
            request.setOrder_status(PaymentStatus.REJECTED.name());
            return Mono.just(request);
        }));
    }

}
