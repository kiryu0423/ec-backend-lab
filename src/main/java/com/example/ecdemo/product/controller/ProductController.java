package com.example.ecdemo.product.controller;

import com.example.ecdemo.product.dto.CreateProductRequest;
import com.example.ecdemo.product.dto.ProductDetailResponse;
import com.example.ecdemo.product.dto.ProductListResponse;
import com.example.ecdemo.product.dto.UpdateProductRequest;
import com.example.ecdemo.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product API" , description = "商品API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "商品一覧取得")
    @GetMapping
    public Page<ProductListResponse> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return productService.getProducts(
            categoryId, keyword, pageable
        );
    }

    @Operation(summary = "商品詳細取得")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(
        @PathVariable Long id,
        @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch
    ) {

        ProductDetailResponse response = productService.getProduct(id);

        String eTag = "\"" + response.id() + "-" + response.version() + "\"";

        if (eTag.equals(ifNoneMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                    .build();
        }

        return ResponseEntity.ok()
                .eTag(eTag)
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                .body(response);
    }

    @Operation(summary = "商品作成")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetailResponse createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.createProduct(request);
    }

    @Operation(summary = "商品更新")
    @PutMapping("/{id}")
    public ProductDetailResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return productService.updateProduct(id, request);
    }

    @Operation(summary = "商品削除")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
