package com.example.ecdemo.common.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long categoryId) {
        super("Category not found. id=" + categoryId);
    }
}
