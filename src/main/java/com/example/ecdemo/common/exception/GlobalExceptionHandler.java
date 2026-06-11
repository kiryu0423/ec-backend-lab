package com.example.ecdemo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        return ErrorResponse.of(
                "RESOURCE_NOT_FOUND",
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(
            MethodArgumentNotValidException e
    ) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");

        return ErrorResponse.of(
                "VALIDATION_ERROR",
                message
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFound(
            ProductNotFoundException e
    ) {
        return ErrorResponse.of(
                "PRODUCT_NOT_FOUND",
                e.getMessage()
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryNotFound(
            CategoryNotFoundException e
    ) {
        return ErrorResponse.of(
                "CATEGORY_NOT_FOUND",
                e.getMessage()
        );
    }

    @ExceptionHandler(TooManyRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleTooManyRequests(
        TooManyRequestsException e
    ) {
        return ErrorResponse.of(
            "TOO_MANY_REQUESTS",
            e.getMessage()
        );
    }

}
