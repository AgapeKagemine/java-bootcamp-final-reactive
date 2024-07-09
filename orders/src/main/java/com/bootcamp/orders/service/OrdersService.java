package com.bootcamp.orders.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.bootcamp.dto.OrderDTO;
import com.bootcamp.dto.OrderRequest;
import com.bootcamp.dto.OrdersItemDTO;
import com.bootcamp.enums.OrderStatus;
import com.bootcamp.orders.config.KafkaProducer;
import com.bootcamp.orders.entity.Orders;
import com.bootcamp.orders.entity.OrdersItem;
import com.bootcamp.orders.repository.OrdersItemRepository;
import com.bootcamp.orders.repository.OrdersRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Validated
@Slf4j
public class OrdersService {

    private OrdersRepository ordersRepository;
    private OrdersItemRepository itemRepository;
    private KafkaProducer kafkaTemplate;

    public OrdersService(OrdersRepository ordersRepository, OrdersItemRepository ordersItemRepository,
            KafkaProducer kafkaTemplate) {
        this.ordersRepository = ordersRepository;
        this.itemRepository = ordersItemRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Flux<Orders> findAllOrders() {
        log.info("Fetching all orders");
        return ordersRepository.findAll();
    }

    public Mono<Orders> getOrderById(Long id) {
        log.info("Fetching order by id: {}", id);
        return ordersRepository.findById(id)
                .switchIfEmpty(Mono.empty());
    }

    public Mono<Orders> createOrder(@Valid OrderRequest request) {
        log.info("Creating order: {}", request);
        Orders orders = new Orders();
        orders.setCustomer_id(request.getCustomer_id());
        orders.setBilling_address(request.getBilling_address());
        orders.setOrder_date(LocalDate.now());
        orders.setOrder_status(OrderStatus.CREATED.name());
        orders.setPayment_method(request.getPayment_method());
        orders.setShipping_address(request.getShipping_address());
        orders.setTotal_amount(0.0);
        return ordersRepository.save(orders).flatMap(order -> {
            OrdersItem ordersItem = new OrdersItem();
            ordersItem.setOrder_id(order.getId());
            ordersItem.setQuantity(request.getOrder_items().getQuantity());
            ordersItem.setProduct_id(request.getOrder_items().getProduct_id());
            ordersItem.setPrice(0.0);
            return itemRepository.save(ordersItem).map(item -> {
                OrdersItemDTO orderItem = new OrdersItemDTO();
                orderItem.setId(item.getId());
                orderItem.setOrder_id(item.getOrder_id());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setProduct_id(item.getProduct_id());
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setId(order.getId());
                orderDTO.setCustomer_id(order.getCustomer_id());
                orderDTO.setOrder_status(order.getOrder_status());
                orderDTO.setOrder_date(order.getOrder_date());
                orderDTO.setPayment_method(order.getPayment_method());
                orderDTO.setOrder_items(orderItem);
                orderDTO.setTotal_amount(0.0);
                kafkaTemplate.sendMessage("order-create", orderDTO);
                return order;
            });
        });
    };

    @KafkaListener(topics = "order-response")
    public void updateOrder(OrderDTO request) {
        log.info("Updating order: {}", request);
        ordersRepository.findById(request.getId()).flatMap(order -> {
            order.setTotal_amount(request.getTotal_amount());
            order.setOrder_status(request.getOrder_status());
            return ordersRepository.save(order).flatMap(o -> {
                return itemRepository.findById(request.getOrder_items().getId()).flatMap(item -> {
                    item.setPrice(request.getOrder_items().getPrice());
                    return itemRepository.save(item);
                });
            });
        }).subscribe();
    }

}