package com.example.ecdemo.product.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductUpdatedEvent(
        String eventId,
        Long productId,
        LocalDateTime occurredAt
) {
    public static ProductUpdatedEvent of(Long productId) {
        return new ProductUpdatedEvent(
                UUID.randomUUID().toString(),
                productId,
                LocalDateTime.now()
        );
    }
}
