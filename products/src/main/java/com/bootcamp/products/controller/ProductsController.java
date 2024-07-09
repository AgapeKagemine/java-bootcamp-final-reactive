package com.bootcamp.products.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.dto.OrderDTO;
import com.bootcamp.products.entity.Products;
import com.bootcamp.products.exception.NotFoundException;
import com.bootcamp.products.service.ProductsService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@Validated
@Slf4j
public class ProductsController {

    private ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public Flux<Products> getAllProducts() {
        log.info("Fetching all products");

        return productsService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Products>> getProductById(@PathVariable Long id) {
        log.info("Fetching product with id: {}", id);

        return productsService.getProductById(id)
                .map(ResponseEntity::ok).switchIfEmpty(Mono.error(new NotFoundException("Product ID not found")));
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<OrderDTO>> add(@RequestBody OrderDTO orderDTO) {
        return productsService.add(orderDTO).map(ResponseEntity::ok);
    }

    @PostMapping("/deduct")
    public Mono<ResponseEntity<OrderDTO>> deduct(@RequestBody OrderDTO orderDTO) {
        return productsService.deduct(orderDTO).map(ResponseEntity::ok);
    }

    // @PostMapping
    // @ResponseStatus(HttpStatus.CREATED)
    // public Mono<Products> createProduct(@Valid @RequestBody ProductsDTO
    // productDTO) {
    // log.info("Received request to create product: {}", productDTO);

    // return productsService.createProduct(productDTO);
    // }

    // @PutMapping("/{id}")
    // public Mono<ResponseEntity<Products>> updateProduct(@PathVariable Long id,
    // @Valid @RequestBody ProductsDTO productDTO) {
    // log.info("Updating product with id: {}", id);

    // return productsService.updateProduct(id, productDTO)
    // .map(ResponseEntity::ok)
    // .defaultIfEmpty(ResponseEntity.notFound().build());
    // }

    // @DeleteMapping("/{id}")
    // public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
    // log.info("Deleting product with id: {}", id);

    // return productsService.deleteProduct(id)
    // .then(Mono.just(ResponseEntity.noContent().<Void>build()))
    // .onErrorResume(ex -> Mono.just(ResponseEntity.notFound().build()));
    // }
}