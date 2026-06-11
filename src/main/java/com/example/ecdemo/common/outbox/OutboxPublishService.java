package com.example.ecdemo.common.outbox;

import com.example.ecdemo.common.config.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxPublishService {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

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
