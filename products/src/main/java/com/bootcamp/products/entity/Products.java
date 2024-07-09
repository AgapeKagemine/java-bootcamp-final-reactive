package com.bootcamp.products.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {

    // id BIGSERIAL PRIMARY KEY,
    // name VARCHAR(255) NOT NULL,
    // price FLOAT(8) NOT NULL,
    // category VARCHAR(255) NOT NULL,
    // created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    // description VARCHAR(255),
    // image_url VARCHAR(255),
    // stock_quantity INTEGER NOT NULL,
    // updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP

    @Id
    private Long id;

    @Size(max = 255, message = "Name must be less than 256 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @Size(max = 255, message = "Category must be less than 256 characters")
    private String category;

    private LocalDate created_at;

    @Size(max = 255, message = "Description must be less than 256 characters")
    private String description;

    @Pattern(regexp = "^https?:\\/\\/.*$", flags = {
            Pattern.Flag.CASE_INSENSITIVE }, message = "Image URL is not valid")
    private String image_url;

    @NotNull(message = "Stock quantity is required")
    private Integer stock_quantity;

    private LocalDate updated_at;

}
