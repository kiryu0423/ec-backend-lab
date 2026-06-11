package com.example.ecdemo.product.event;

import com.example.ecdemo.common.config.RabbitMqConfig;
import com.example.ecdemo.common.event.ProcessedEvent;
import com.example.ecdemo.common.event.ProcessedEventRepository;
import com.example.ecdemo.product.search.ProductSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDeletedConsumer {

    private final ProcessedEventRepository processedEventRepository;
    private final ProductSearchService productSearchService;
    private final ObjectMapper objectMapper;

    @Transactional
    @RabbitListener(queues = RabbitMqConfig.PRODUCT_DELETED_QUEUE)
    public void consume(String payload) {
        ProductDeletedEvent event = toEvent(payload);

        if (processedEventRepository.existsById(event.eventId())) {
            log.info("Duplicated deleted event skipped. eventId={}", event.eventId());
            return;
        }

        productSearchService.delete(event.productId());

        processedEventRepository.save(
                new ProcessedEvent(event.eventId())
        );

        log.info(
                "Product deleted from OpenSearch. eventId={}, productId={}, occurredAt={}",
                event.eventId(),
                event.productId(),
                event.occurredAt()
        );
    }

    private ProductDeletedEvent toEvent(String payload) {
        try {
            return objectMapper.readValue(payload, ProductDeletedEvent.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid deleted event payload", e);
        }
    }
}
