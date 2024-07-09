package com.bootcamp.dto;

import java.time.LocalDate;

import com.bootcamp.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long customer_id;
    private Double amount;
    private Long order_id;
    private LocalDate payment_date;
    private String mode;
    private PaymentStatus status;
    private String reference_number;

}
