package com.java.challenge.store.service.impl;

import com.java.challenge.store.dto.ProductRequestDto;
import com.java.challenge.store.dto.ProductResponseDto;
import com.java.challenge.store.entity.Product;
import com.java.challenge.store.exception.ResourceNotFoundException;
import com.java.challenge.store.repository.ProductRepository;
import com.java.challenge.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Mono<ProductResponseDto> create(ProductRequestDto request) {
        return Mono.defer(() -> {
            Product product = new Product(
                    null,
                    request.getName(),
                    request.getPrice(),
                    request.getStock(),
                    null
            );
            return repository.save(product);
        }).map(this::toResponse);
    }

    @Override
    public Mono<ProductResponseDto> findById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Product not found")
                ))
                .map(this::toResponse);
    }

    @Override
    public Flux<ProductResponseDto> findAll() {
        return repository.findAll()
                .map(this::toResponse);
    }

    @Override
    public Mono<ProductResponseDto> update(Long id, ProductRequestDto request) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Product not found")
                ))
                .flatMap(existing ->
                        Mono.defer(() -> {
                                    existing.setName(request.getName());
                                    existing.setPrice(request.getPrice());
                                    existing.setStock(request.getStock());
                                    return repository.save(existing);
                                })
                ).map(this::toResponse);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Product not found")
                ))
                .flatMap(repository::delete);
    }

    private ProductResponseDto toResponse(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}

