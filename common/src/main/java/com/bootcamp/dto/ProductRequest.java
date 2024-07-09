package com.bootcamp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private Long id;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    private Integer stock_quantity;

}
