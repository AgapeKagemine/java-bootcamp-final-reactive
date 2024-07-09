package com.bootcamp.orders.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.bootcamp.orders.entity.Orders;

public interface OrdersRepository extends R2dbcRepository<Orders, Long> {

}
