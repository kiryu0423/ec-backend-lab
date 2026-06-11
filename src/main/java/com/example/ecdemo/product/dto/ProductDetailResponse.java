package com.example.ecdemo.product.dto;

import com.example.ecdemo.product.entity.Product;

import java.time.LocalDateTime;

public record ProductDetailResponse(
        Long id,
        String name,
        String description,
        Integer price,
        Integer stock,
        String categoryName,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getName(),
                product.getVersion(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
