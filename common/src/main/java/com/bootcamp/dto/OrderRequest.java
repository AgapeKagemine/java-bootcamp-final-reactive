package com.bootcamp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Billing address is mandatory")
    private String billing_address;

    @NotNull(message = "Customer ID is mandatory")
    @Positive
    private Long customer_id;

    @NotBlank(message = "Payment method is mandatory")
    private String payment_method;

    @NotBlank(message = "Shipping address is mandatory")
    private String shipping_address;

    @Valid
    private OrdersItemRequestDTO order_items;

}
