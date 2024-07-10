package com.bootcamp.products.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootcamp.dto.OrderDTO;
import com.bootcamp.enums.ProductStatus;
import com.bootcamp.products.entity.Products;
import com.bootcamp.products.repository.ProductsRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductsService {

    private ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Flux<Products> getAllProducts() {
        log.info("Fetching all products");
        return productsRepository.findAll();
    }

    public Mono<Products> getProductById(Long id) {
        log.info("Fetching product by id: {}", id);
        return productsRepository.findById(id).switchIfEmpty(Mono.empty());
    }

    @Transactional
    public Mono<OrderDTO> deduct(OrderDTO orderDTO) {
        log.info("Request: " + orderDTO);
        return productsRepository.findById(orderDTO.getOrder_items().getProduct_id()).flatMap(item -> {
            if (orderDTO.getOrder_items().getQuantity() <= item.getStock_quantity()) {
                item.setStock_quantity(item.getStock_quantity() - orderDTO.getOrder_items().getQuantity());
                item.setUpdated_at(LocalDate.now());
                orderDTO.getOrder_items().setPrice(item.getPrice());
                orderDTO.setTotal_amount(item.getPrice() * orderDTO.getOrder_items().getQuantity());
                orderDTO.setOrder_status(ProductStatus.INSTOCK.name());
                productsRepository.save(item).subscribe();
            } else {
                orderDTO.setOrder_status(ProductStatus.OUTOFSTOCK.name());
            }
            return Mono.just(orderDTO);
        }).switchIfEmpty(Mono.defer(() -> {
            orderDTO.setOrder_status(ProductStatus.OUTOFSTOCK.name());
            return Mono.just(orderDTO);
        }));
    }

    @Transactional
    public Mono<OrderDTO> add(OrderDTO orderDTO) {
        log.info("Rolling Back: " + orderDTO);
        return productsRepository.findById(orderDTO.getOrder_items().getProduct_id()).flatMap(item -> {
            item.setStock_quantity(item.getStock_quantity() + orderDTO.getOrder_items().getQuantity());
            item.setUpdated_at(LocalDate.now());
            productsRepository.save(item).subscribe();
            return Mono.just(orderDTO);
        });
    }

}
