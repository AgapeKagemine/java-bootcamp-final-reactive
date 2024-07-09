package com.bootcamp.payment.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.bootcamp.payment.model.Payments;

public interface PaymentsRepository extends R2dbcRepository<Payments, Long> {

}
