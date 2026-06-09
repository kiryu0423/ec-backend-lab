package com.example.ecdemo.product.search;

import com.example.ecdemo.product.entity.Product;

public record ProductDocument(
        Long id,
        String name,
        String description,
        Integer price,
        Integer stock,
        Long categoryId,
        String categoryName
) {
    public static ProductDocument from(Product product) {
        return new ProductDocument(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
