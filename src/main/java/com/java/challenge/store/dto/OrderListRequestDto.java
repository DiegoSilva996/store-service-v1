package com.java.challenge.store.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderListRequestDto {

    @NotEmpty(message = "items must not be empty")
    @Valid
    private List<OrderItemDto> items;
}
