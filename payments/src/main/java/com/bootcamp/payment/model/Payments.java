package com.bootcamp.payment.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payments {

    // id BIGSERIAL PRIMARY KEY,
    // amount FLOAT(8) NOT NULL,
    // order_id FLOAT(8) NOT NULL,
    // payment_date TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    // mode VARCHAR(255) NOT NULL,
    // status VARCHAR(255) NOT NULL,
    // reference_number VARCHAR(255) NOT NULL

    @Id
    private Long id;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Order ID cannot be null")
    @Positive(message = "Order ID must be positive")
    private Long order_id;

    @NotNull(message = "Payment date cannot be null")
    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDate payment_date;

    @NotBlank(message = "Mode cannot be blank")
    private String mode;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotBlank(message = "Reference number cannot be blank")
    private String reference_number;

}
