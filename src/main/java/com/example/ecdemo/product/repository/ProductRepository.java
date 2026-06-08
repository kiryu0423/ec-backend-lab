package com.example.ecdemo.product.repository;

import com.example.ecdemo.product.dto.ProductListResponse;
import com.example.ecdemo.product.entity.Product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        select new com.example.ecdemo.product.dto.ProductListResponse(
            p.id,
            p.name,
            p.price,
            c.name
        )
        from Product p
        join p.category c
        where (:categoryId is null or c.id = :categoryId)
        and (:keyword = '' or lower(p.name) like lower(concat('%', :keyword, '%')))
    """)
    Page<ProductListResponse> findProductList(
        Long categoryId,
        String keyword,
        Pageable pageable
    );

    @Query("""
        select p
        from Product p
        join fetch p.category
        where p.id = :id
    """)
    Optional<Product> findByIdWithCategory(Long id);

}
