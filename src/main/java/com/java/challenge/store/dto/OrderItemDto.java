package com.java.challenge.store.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemDto {

    @NotNull(message = "productId is required")
    private Long productId;

    @Positive(message = "quantity must be greater than zero")
    private Integer quantity;
}
