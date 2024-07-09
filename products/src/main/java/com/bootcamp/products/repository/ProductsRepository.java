package com.bootcamp.products.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.bootcamp.products.entity.Products;

public interface ProductsRepository extends R2dbcRepository<Products, Long> {

}
