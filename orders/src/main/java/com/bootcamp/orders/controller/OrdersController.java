package com.bootcamp.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.dto.OrderRequest;
import com.bootcamp.orders.entity.Orders;
import com.bootcamp.orders.service.OrdersService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@Validated
@Slf4j
public class OrdersController {

    private OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping
    public Flux<Orders> getAll() {
        log.info("Fetching all orders");
        return ordersService.findAllOrders();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Orders>> getOrderById(@PathVariable @NotNull @Min(1) Long id) {
        log.info("Fetching order by id: {}", id);

        return ordersService.getOrderById(id)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<Orders>> create(@RequestBody @Valid OrderRequest request) {
        return ordersService.createOrder(request)
                .map(ResponseEntity::ok);
    }

}
