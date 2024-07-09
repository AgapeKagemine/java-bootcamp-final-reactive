package com.bootcamp.dto;

import com.bootcamp.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private Double price;
    private String category;
    private String description;
    private String image_url;
    private Integer stock_quantity;
    private ProductStatus status;

}
