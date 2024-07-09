package com.bootcamp.orders.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("orders")
public class Orders {

    @Id
    private Long id;

    @NotBlank(message = "Billing address is mandatory")
    private String billing_address;

    @NotNull(message = "Customer ID is mandatory")
    private Long customer_id;

    private LocalDate order_date;

    @NotBlank(message = "Order status is mandatory")
    private String order_status;

    @NotBlank(message = "Payment method is mandatory")
    private String payment_method;

    @NotBlank(message = "Shipping address is mandatory")
    private String shipping_address;

    @NotNull(message = "Total amount is mandatory")
    @Positive(message = "Total amount must be positive")
    private Double total_amount;

}
