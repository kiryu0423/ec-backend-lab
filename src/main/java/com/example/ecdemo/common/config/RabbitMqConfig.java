package com.example.ecdemo.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_UPDATED_QUEUE = "product.updated.queue";
    public static final String PRODUCT_UPDATED_ROUTING_KEY = "product.updated";
    public static final String PRODUCT_DLX = "product.dlx";
    public static final String PRODUCT_UPDATED_DLQ = "product.updated.dlq";
    public static final String PRODUCT_UPDATED_DLQ_ROUTING_KEY = "product.updated.dlq";
    public static final String PRODUCT_DELETED_QUEUE = "product.deleted.queue";
    public static final String PRODUCT_DELETED_ROUTING_KEY = "product.deleted";
    public static final String PRODUCT_DELETED_DLQ = "product.deleted.dlq";
    public static final String PRODUCT_DELETED_DLQ_ROUTING_KEY = "product.deleted.dlq";

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Queue productUpdatedQueue() {
        return QueueBuilder.durable(PRODUCT_UPDATED_QUEUE)
                .withArgument("x-dead-letter-exchange", PRODUCT_DLX)
                .withArgument("x-dead-letter-routing-key", PRODUCT_UPDATED_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding productUpdatedBinding() {
        return BindingBuilder
                .bind(productUpdatedQueue())
                .to(productExchange())
                .with(PRODUCT_UPDATED_ROUTING_KEY);
    }

    @Bean
    public DirectExchange productDlx() {
        return new DirectExchange(PRODUCT_DLX);
    }

    @Bean
    public Queue productUpdatedDlq() {
        return QueueBuilder.durable(PRODUCT_UPDATED_DLQ).build();
    }

    @Bean
    public Binding productUpdatedDlqBinding() {
        return BindingBuilder
                .bind(productUpdatedDlq())
                .to(productDlx())
                .with(PRODUCT_UPDATED_DLQ_ROUTING_KEY);
    }

    @Bean
    public Queue productDeletedQueue() {
        return QueueBuilder.durable(PRODUCT_DELETED_QUEUE)
                .withArgument("x-dead-letter-exchange", PRODUCT_DLX)
                .withArgument("x-dead-letter-routing-key", PRODUCT_DELETED_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding productDeletedBinding() {
        return BindingBuilder
                .bind(productDeletedQueue())
                .to(productExchange())
                .with(PRODUCT_DELETED_ROUTING_KEY);
    }

    @Bean
    public Queue productDeletedDlq() {
        return QueueBuilder.durable(PRODUCT_DELETED_DLQ).build();
    }

    @Bean
    public Binding productDeletedDlqBinding() {
        return BindingBuilder
                .bind(productDeletedDlq())
                .to(productDlx())
                .with(PRODUCT_DELETED_DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
