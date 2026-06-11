package com.example.ecdemo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisLockConfig {

    @Bean
    public RedisLockRegistry redisLockRegistry(
            RedisConnectionFactory redisConnectionFactory
    ) {
        return new RedisLockRegistry(
                redisConnectionFactory,
                "ec-demo-lock",
                10_000L
        );
    }
}
