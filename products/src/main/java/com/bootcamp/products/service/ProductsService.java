package com.bootcamp.products.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

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
        return productsRepository.findById(id);
    }

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
        }).switchIfEmpty(Mono.just(orderDTO));
    }

    public Mono<OrderDTO> add(OrderDTO orderDTO) {
        return productsRepository.findById(orderDTO.getOrder_items().getProduct_id()).flatMap(item -> {
            item.setStock_quantity(item.getStock_quantity() + orderDTO.getOrder_items().getQuantity());
            item.setUpdated_at(LocalDate.now());
            productsRepository.save(item).subscribe();
            return Mono.just(orderDTO);
        });
    }

    // @Transactional
    // public Mono<Products> createProduct(@NotNull @Valid ProductsDTO productDTO) {
    // log.info("Creating product: {}", productDTO);

    // Products product = mapToEntity(productDTO);
    // return productsRepository.save(product);
    // // .doOnSuccess(savedPayment -> log.info("Payment created: {}",
    // savedPayment))
    // // .doOnError(error -> log.error("Error creating payment: {}",
    // // error.getMessage()))
    // // .onErrorMap(e -> {
    // // log.error("Error creating product: {}", e.getMessage());
    // // return new RequestException("Failed to create product");
    // // });
    // }

    // @Transactional
    // public Mono<Products> updateProduct(Long id, @NotNull @Valid ProductsDTO
    // productDTO) {
    // log.info("Updating product with id {}: {}", id, productDTO);

    // return productsRepository.findById(id)
    // .flatMap(f -> {
    // Products product = mapToEntity(productDTO);
    // product.setId(id);
    // product.setCreated_at(f.getCreated_at());
    // product.setUpdated_at(LocalDate.now());
    // return productsRepository.save(product);
    // });
    // // .doOnSuccess(savedPayment -> log.info("Payment created: {}",
    // savedPayment))
    // // .doOnError(error -> log.error("Error creating payment: {}",
    // // error.getMessage()))
    // // .onErrorMap(e -> {
    // // log.error("Error updating product: {}", e.getMessage());
    // // return new RequestException("Failed to update product");
    // // })
    // // .switchIfEmpty(Mono.error(new RequestException("Product with id " + id + "
    // // not found")));

    // }

    // @Transactional
    // public Mono<Void> deleteProduct(Long id) {
    // log.info("Deleting product with id: {}", id);

    // return productsRepository.deleteById(id);
    // // .doOnSuccess(savedPayment -> log.info("Payment created: {}",
    // savedPayment))
    // // .doOnError(error -> log.error("Error creating payment: {}",
    // // error.getMessage()))
    // // .onErrorMap(e -> {
    // // log.error("Error deleting product with id {}: {}", id, e.getMessage());
    // // return new RequestException("Failed to delete product with id " + id);
    // // });
    // }

    // private Products mapToEntity(ProductsDTO productDTO) {
    // Products product = new Products();
    // product.setName(productDTO.getName());
    // product.setPrice(productDTO.getPrice());
    // product.setCategory(productDTO.getCategory());
    // product.setDescription(productDTO.getDescription());
    // product.setImage_url(productDTO.getImage_url());
    // product.setStock_quantity(productDTO.getStock_quantity());
    // return product;
    // }

}
