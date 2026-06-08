package com.example.ecdemo.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank
        @Size(max = 255)
        String name,

        String description,

        @NotNull
        @PositiveOrZero
        Integer price,

        @NotNull
        @PositiveOrZero
        Integer stock,

        @NotNull
        Long categoryId
) {
}
