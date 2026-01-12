package com.java.challenge.store.service;

import com.java.challenge.store.dto.ProductRequestDto;
import com.java.challenge.store.dto.ProductResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<ProductResponseDto> create(ProductRequestDto request);

    Mono<ProductResponseDto> findById(Long id);

    Flux<ProductResponseDto> findAll();

    Mono<ProductResponseDto> update(Long id, ProductRequestDto request);

    Mono<Void> delete(Long id);
}

