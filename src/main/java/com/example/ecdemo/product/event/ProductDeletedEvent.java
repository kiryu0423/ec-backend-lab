package com.example.ecdemo.product.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDeletedEvent(
        String eventId,
        Long productId,
        LocalDateTime occurredAt
) {
    public static ProductDeletedEvent of(Long productId) {
        return new ProductDeletedEvent(
                UUID.randomUUID().toString(),
                productId,
                LocalDateTime.now()
        );
    }
}
