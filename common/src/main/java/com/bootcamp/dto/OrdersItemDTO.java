package com.bootcamp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersItemDTO {

    private Long id;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Product ID is mandatory")
    private Long product_id;

    @NotNull(message = "Quantity is mandatory")
    @PositiveOrZero(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Order ID is mandatory")
    private Long order_id;

}
