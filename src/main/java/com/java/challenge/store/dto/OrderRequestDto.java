package com.java.challenge.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRequestDto {

    @NotNull(message = "orderDate is required")
    private LocalDateTime orderDate;
    @NotNull(message = "total is required")
    @Positive(message = "total must be greater than zero")
    private BigDecimal total;
    @NotBlank(message = "status is required")
    private String status;
}
