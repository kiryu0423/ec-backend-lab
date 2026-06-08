package com.example.ecdemo.common.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long productId) {
        super("Product not found. id=" + productId);
    }
}
