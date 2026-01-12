package com.java.challenge.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
}
