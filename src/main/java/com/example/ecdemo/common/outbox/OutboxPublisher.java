package com.example.ecdemo.common.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final RedisLockRegistry redisLockRegistry;
    private final OutboxPublishService outboxPublishService;

    @Scheduled(fixedDelay = 5000)
    public void publish() {
        Lock lock = redisLockRegistry.obtain("outbox-publisher");

        boolean acquired = lock.tryLock();

        if (!acquired) {
            log.info("Outbox publish skipped because lock was not acquired.");
            return;
        }

        try {
            outboxPublishService.publish();
        } finally {
            lock.unlock();
        }
    }
}
