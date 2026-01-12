package com.java.challenge.store.service.impl;

import com.java.challenge.store.dto.OrderItemDto;
import com.java.challenge.store.dto.OrderListRequestDto;
import com.java.challenge.store.dto.OrderRequestDto;
import com.java.challenge.store.dto.OrderResponseDto;
import com.java.challenge.store.entity.Order;
import com.java.challenge.store.entity.Product;
import com.java.challenge.store.exception.BusinessException;
import com.java.challenge.store.exception.ResourceNotFoundException;
import com.java.challenge.store.repository.OrderRepository;
import com.java.challenge.store.repository.ProductRepository;
import com.java.challenge.store.service.OrderService;
import com.java.challenge.store.util.DiscountCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<OrderResponseDto> createOrder(OrderListRequestDto orderListRequestDto) {

        return Flux.fromIterable(orderListRequestDto.getItems())
                .flatMap(item ->
                        productRepository.findById(item.getProductId())
                                .switchIfEmpty(Mono.error(
                                        new BusinessException("Product for check items not found")
                                ))
                                .flatMap(producto -> validateAndUpdateStock(producto, item))
                )
                .collectList()
                .flatMap(updatedProducts -> createOrder(updatedProducts, orderListRequestDto))
                .map(this::toResponseDto)
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<OrderResponseDto> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Order not found")
                ))
                .map(this::toResponseDto);
    }

    @Override
    public Flux<OrderResponseDto> findAll() {
        return orderRepository.findAll()
                .map(this::toResponseDto);
    }

    @Override
    public Mono<OrderResponseDto> update(Long id, OrderRequestDto request) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Order not found")
                ))
                .flatMap(order -> {
                    order.setStatus(request.getStatus());
                    order.setTotal(request.getTotal());
                    order.setDate(request.getOrderDate());
                    return orderRepository.save(order);
                })
                .map(this::toResponseDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new ResourceNotFoundException("Order not found")
                ))
                .flatMap(orderRepository::delete);
    }

    private Mono<Product> validateAndUpdateStock(
            Product product,
            OrderItemDto item
    ) {
        return Mono.defer(() -> {
            if (product.getStock() < item.getQuantity()) {
                return Mono.error(
                        new BusinessException("Insufficient stock for product " + product.getId())
                );
            }

            product.setStock(product.getStock() - item.getQuantity());
            return productRepository.save(product)
                    .onErrorMap(
                            OptimisticLockingFailureException.class,
                            ex -> new BusinessException("Concurrent stock update detected, please retry")
                    );
        });
    }

    private Mono<Order> createOrder(
            List<Product> products,
            OrderListRequestDto request
    ) {
        BigDecimal total = getTotalPrice(products, request);

        BigDecimal totalWithDiscount =
                DiscountCalculator.applyDiscount(total, products.size());

        Order order = new Order(
                null,
                LocalDateTime.now(),
                totalWithDiscount,
                "CONFIRMED ORDER."
        );

        return orderRepository.save(order);
    }

    private BigDecimal getTotalPrice(
            List<Product> products,
            OrderListRequestDto orderListRequestDto
    ) {
        Map<Long, Integer> orders = orderListRequestDto.getItems().stream()
                .collect(Collectors.toMap(
                        OrderItemDto::getProductId,
                        OrderItemDto::getQuantity
                ));

        return products.stream()
                .map(p ->
                        p.getPrice().multiply(
                                BigDecimal.valueOf(orders.get(p.getId()))
                        )
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getDate(),
                order.getTotal(),
                order.getStatus()
        );
    }
}
