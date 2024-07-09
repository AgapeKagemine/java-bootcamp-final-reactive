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
        return ordersService.findAllOrders()
                .doOnError(err -> log.error("Error fetchinbg all orders " + err.getMessage()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Orders>> getOrderById(@PathVariable @NotNull @Min(1) Long id) {
        log.info("Fetching order by id: {}", id);

        return ordersService.getOrderById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(err -> log.error("Error fetching order with id {}: {}", id,
                        err.getMessage()));
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<Orders>> create(@RequestBody @Valid OrderRequest request) {
        return ordersService.createOrder(request)
                .map(ResponseEntity::ok)
                .doOnError(err -> log.error("Error creating order " + err.getMessage()));
    }

    // @PostMapping
    // public Mono<ResponseEntity<Orders>> createOrder(@RequestBody @Valid OrdersDTO
    // ordersDTO) {
    // log.info("Received request to create order: {}", ordersDTO);

    // return ordersService.createOrder(ordersDTO)
    // .map(createdOrder ->
    // ResponseEntity.status(HttpStatus.CREATED).body(createdOrder))
    // .doOnError(error -> log.error("Error creating order: {}",
    // error.getMessage()));
    // }

    // @GetMapping
    // public Flux<Orders> getAllOrders() {
    // log.info("Fetching all orders");

    // return ordersService.getAllOrders();
    // }

    // @GetMapping("/{id}")
    // public Mono<ResponseEntity<Orders>> getOrderById(@PathVariable @NotNull
    // @Min(1) Long id) {
    // log.info("Fetching order by id: {}", id);

    // return ordersService.getOrderById(id)
    // .map(ResponseEntity::ok)
    // .defaultIfEmpty(ResponseEntity.notFound().build())
    // .doOnError(error -> log.error("Error fetching order with id {}: {}", id,
    // error.getMessage()));
    // }

    // @PutMapping("/{id}")
    // public Mono<ResponseEntity<Void>> updateOrder(@PathVariable @NotNull @Min(1)
    // Long id,
    // @RequestBody @Valid OrdersDTO ordersDTO) {
    // log.info("Updating order with id {}: {}", id, ordersDTO);

    // return ordersService.updateOrder(id, ordersDTO)
    // .then(Mono.just(ResponseEntity.ok().<Void>build()))
    // .doOnError(error -> log.error("Error updating order with id {}: {}", id,
    // error.getMessage()))
    // .onErrorResume(err ->
    // Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    // }

    // @DeleteMapping("/{id}")
    // public Mono<ResponseEntity<Void>> deleteOrder(@PathVariable @NotNull @Min(1)
    // Long id) {
    // log.info("Deleting order with id: {}", id);

    // return ordersService.deleteOrder(id)
    // .then(Mono.just(ResponseEntity.ok().<Void>build()))
    // .doOnError(error -> log.error("Error deleting order with id {}: {}", id,
    // error.getMessage()))
    // .onErrorResume(err ->
    // Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    // }

}
