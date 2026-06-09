package com.example.ecdemo.product.service;

import com.example.ecdemo.category.entity.Category;
import com.example.ecdemo.category.repository.CategoryRepository;
import com.example.ecdemo.common.config.RabbitMqConfig;
import com.example.ecdemo.common.exception.CategoryNotFoundException;
import com.example.ecdemo.common.exception.ProductNotFoundException;
import com.example.ecdemo.common.outbox.OutboxEvent;
import com.example.ecdemo.common.outbox.OutboxEventRepository;
import com.example.ecdemo.product.dto.CreateProductRequest;
import com.example.ecdemo.product.dto.ProductDetailResponse;
import com.example.ecdemo.product.dto.ProductListResponse;
import com.example.ecdemo.product.dto.UpdateProductRequest;
import com.example.ecdemo.product.entity.Product;
import com.example.ecdemo.product.event.ProductDeletedEvent;
import com.example.ecdemo.product.event.ProductUpdatedEvent;
import com.example.ecdemo.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public Page<ProductListResponse> getProducts(
        Long categoryId,
        String keyword,
        Pageable pageable
    ) {
        String normalizedKeyword = (keyword == null) ? "" : keyword.trim();

        return productRepository.findProductList(
            categoryId,
            normalizedKeyword,
            pageable
        );
    }

    @Cacheable(value = "productDetail", key = "#id")
    public ProductDetailResponse getProduct(Long id) {
        Product product = productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductDetailResponse.from(product);
    }

    @Transactional
    public ProductDetailResponse createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                category
        );

        Product savedProduct = productRepository.save(product);

        return ProductDetailResponse.from(savedProduct);
    }

    @Transactional
    @CacheEvict(value = "productDetail", key = "#id")
    public ProductDetailResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        product.update(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                category
        );

        ProductUpdatedEvent event = ProductUpdatedEvent.of(id);
        String payload = toJson(event);

        outboxEventRepository.save(
            new OutboxEvent(
                event.eventId(),
                "ProductUpdatedEvent",
                RabbitMqConfig.PRODUCT_UPDATED_ROUTING_KEY,
                payload
            )
        );

        return ProductDetailResponse.from(product);
    }

    @Transactional
    @CacheEvict(value = "productDetail", key = "#id")
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);

        ProductDeletedEvent event = ProductDeletedEvent.of(id);
        String payload = toJson(event);

        outboxEventRepository.save(
                new OutboxEvent(
                        event.eventId(),
                        "ProductDeletedEvent",
                        RabbitMqConfig.PRODUCT_DELETED_ROUTING_KEY,
                        payload
                )
        );
    }

    private String toJson(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize event", e);
        }
    }
}
