package com.example.ecdemo.product.service;

import com.example.ecdemo.category.entity.Category;
import com.example.ecdemo.category.repository.CategoryRepository;
import com.example.ecdemo.common.exception.CategoryNotFoundException;
import com.example.ecdemo.common.exception.ProductNotFoundException;
import com.example.ecdemo.product.dto.CreateProductRequest;
import com.example.ecdemo.product.dto.ProductDetailResponse;
import com.example.ecdemo.product.dto.ProductListResponse;
import com.example.ecdemo.product.dto.UpdateProductRequest;
import com.example.ecdemo.product.entity.Product;
import com.example.ecdemo.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

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

        return ProductDetailResponse.from(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
    }
}
