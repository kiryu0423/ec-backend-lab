package com.example.ecdemo.common.outbox;

import com.example.ecdemo.common.config.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publish() {
        List<OutboxEvent> events =
                outboxEventRepository.findTop100ByStatusOrderByCreatedAtAsc(
                        OutboxStatus.NEW
                );

        for (OutboxEvent event : events) {
            rabbitTemplate.convertAndSend(
                    RabbitMqConfig.PRODUCT_EXCHANGE,
                    event.getRoutingKey(),
                    event.getPayload()
            );

            event.markPublished();

            log.info(
                    "Outbox event published. eventId={}, eventType={}",
                    event.getEventId(),
                    event.getEventType()
            );
        }
    }
}
