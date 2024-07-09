package com.bootcamp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private Long customer_id;
    private LocalDate order_date;
    private String order_status;
    private String payment_method;
    private Double total_amount;
    private OrdersItemDTO order_items;

}
