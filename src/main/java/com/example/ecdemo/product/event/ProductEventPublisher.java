package com.example.ecdemo.product.event;

import com.example.ecdemo.common.config.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishProductUpdated(Long productId) {
        ProductUpdatedEvent event = ProductUpdatedEvent.of(productId);

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PRODUCT_EXCHANGE,
                RabbitMqConfig.PRODUCT_UPDATED_ROUTING_KEY,
                event
        );
    }
}
