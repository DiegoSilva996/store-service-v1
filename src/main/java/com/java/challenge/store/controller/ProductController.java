package com.java.challenge.store.controller;

import com.java.challenge.store.dto.ProductRequestDto;
import com.java.challenge.store.dto.ProductResponseDto;
import com.java.challenge.store.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management controller")
public class ProductController {

    private final ProductService productService;


    @Operation(
            summary = "Create product",
            description = "Create a product and it is stored in the database",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body with data of the product",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Create product successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PostMapping
    public Mono<ResponseEntity<ProductResponseDto>> create(
            @Valid @RequestBody ProductRequestDto request
    ) {
        return productService.create(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @Operation(
            summary = "Search product with ID",
            description = "Search product by its identifier",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get product successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseDto>> findById(
            @PathVariable Long id
    ) {
        return productService.findById(id)
                .map(ResponseEntity::ok);
    }

    @Operation(
            summary = "Get all products",
            description = "Retrieve all products stored in the database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Products retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public Flux<ProductResponseDto> findAll() {
        return productService.findAll();
    }

    @Operation(
            summary = "Update product",
            description = "Update an existing product using its ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body with updated product data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto request
    ) {
        return productService.update(id, request)
                .map(ResponseEntity::ok);
    }

    @Operation(
            summary = "Delete product",
            description = "Delete a product from the database using its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable Long id
    ) {
        return productService.delete(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}

