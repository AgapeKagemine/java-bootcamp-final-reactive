package com.bootcamp.payment.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.bootcamp.payment.model.Balance;

public interface BalanceRepository extends R2dbcRepository<Balance, Long> {

}
