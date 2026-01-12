package com.java.challenge.store.service;

import com.java.challenge.store.dto.OrderListRequestDto;
import com.java.challenge.store.dto.OrderRequestDto;
import com.java.challenge.store.dto.OrderResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OrderService {

    Mono<OrderResponseDto> createOrder(OrderListRequestDto request);

    Mono<OrderResponseDto> findById(Long id);

    Flux<OrderResponseDto> findAll();

    Mono<OrderResponseDto> update(Long id, OrderRequestDto request);

    Mono<Void> delete(Long id);
}
