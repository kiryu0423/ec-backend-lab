package com.example.ecdemo.category.repository;

import com.example.ecdemo.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
