package com.bootcamp.orders.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("orders_item")
public class OrdersItem {

    // id BIGSERIAL PRIMARY KEY,
    // price FLOAT(8) NOT NULL,
    // product_id INTEGER NOT NULL,
    // quantity INTEGER NOT NULL,
    // order_id BIGINT NOT NULL,

    @Id
    private Long id;

    @NotNull(message = "Price is mandatory")
    @PositiveOrZero(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Product ID is mandatory")
    private Long product_id;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Order ID is mandatory")
    private Long order_id;
}
