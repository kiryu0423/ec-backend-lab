package com.example.ecdemo.product.event;

import com.example.ecdemo.common.config.RabbitMqConfig;
import com.example.ecdemo.common.event.ProcessedEvent;
import com.example.ecdemo.common.event.ProcessedEventRepository;
import com.example.ecdemo.common.exception.ProductNotFoundException;
import com.example.ecdemo.product.entity.Product;
import com.example.ecdemo.product.repository.ProductRepository;
import com.example.ecdemo.product.search.ProductDocument;
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
public class ProductUpdatedConsumer {

    private final ProcessedEventRepository processedEventRepository;
    private final ProductRepository productRepository;
    private final ProductSearchService productSearchService;
    private final ObjectMapper objectMapper;

    @Transactional
    @RabbitListener(queues = RabbitMqConfig.PRODUCT_UPDATED_QUEUE)
    public void consume(String payload) {
        ProductUpdatedEvent event = toEvent(payload);

        if (processedEventRepository.existsById(event.eventId())) {
            log.info("Duplicated event skipped. eventId={}", event.eventId());
            return;
        }

        Product product = productRepository.findByIdWithCategory(event.productId())
                .orElseThrow(() -> new ProductNotFoundException(event.productId()));

        ProductDocument document = ProductDocument.from(product);

        productSearchService.index(document);

        processedEventRepository.save(
                new ProcessedEvent(event.eventId())
        );

        log.info(
                "Product indexed to OpenSearch. eventId={}, productId={}, occurredAt={}",
                event.eventId(),
                event.productId(),
                event.occurredAt()
        );
    }

    private ProductUpdatedEvent toEvent(String payload) {
        try {
            return objectMapper.readValue(payload, ProductUpdatedEvent.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event payload", e);
        }
    }
}
