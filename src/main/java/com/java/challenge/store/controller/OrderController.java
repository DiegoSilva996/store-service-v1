package com.java.challenge.store.controller;

import com.java.challenge.store.dto.*;
import com.java.challenge.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management controller")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Create order",
            description = "Creates a new order and updates product stock atomically",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderListRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Order created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Error in business flowBu",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping
    public Mono<ResponseEntity<OrderResponseDto>> create(
            @Valid @RequestBody OrderListRequestDto request
    ) {
        return orderService.createOrder(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @Operation(
            summary = "Get order by ID",
            description = "Retrieves an order using its unique identifier",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Order ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrderResponseDto>> findById(
            @PathVariable Long id
    ) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(
            summary = "List all orders",
            description = "Returns all orders stored in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of orders",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public Flux<OrderResponseDto> findAll() {
        return orderService.findAll();
    }

    @Operation(
            summary = "Update order",
            description = "Updates order information such as status or total",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Order ID",
                            required = true,
                            example = "1"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PutMapping("/{id}")
    public Mono<ResponseEntity<OrderResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDto request
    ) {
        return orderService.update(id, request)
                .map(ResponseEntity::ok);
    }

    @Operation(
            summary = "Delete order",
            description = "Deletes an existing order by ID",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Order ID",
                            required = true,
                            example = "1"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Order deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable Long id
    ) {
        return orderService.delete(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
