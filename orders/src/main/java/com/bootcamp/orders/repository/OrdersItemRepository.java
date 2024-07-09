package com.bootcamp.orders.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.bootcamp.orders.entity.OrdersItem;

public interface OrdersItemRepository extends R2dbcRepository<OrdersItem, Long> {

}
