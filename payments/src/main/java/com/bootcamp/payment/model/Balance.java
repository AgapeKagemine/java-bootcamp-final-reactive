package com.bootcamp.payment.model;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    // id BIGSERIAL PRIMARY KEY,
    // amount FLOAT(8) NOT NULL

    @Id
    private Long id;

    @NotNull(message = "Amount cannot be null")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private Double amount;

}
