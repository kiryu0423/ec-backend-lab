package com.example.ecdemo.product.dto;

import com.example.ecdemo.product.entity.Product;

public record ProductListResponse(
        Long id,
        String name,
        Integer price,
        String categoryName
) {
    public static ProductListResponse from(Product product) {
        return new ProductListResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory().getName()
        );
    }
}
