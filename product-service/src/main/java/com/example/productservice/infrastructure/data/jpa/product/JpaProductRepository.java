package com.example.productservice.infrastructure.data.jpa.product;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query(value = "SELECT * FROM products WHERE products.id IN (SELECT id FROM products WHERE name LIKE %?1% OR description LIKE %?1%) AND category_id = ?2", nativeQuery = true)
    Page<ProductEntity> getProductBySearchQueryAndCategory(String search, Long categoryId, Pageable pageable);
    @Query(value = "SELECT * FROM products WHERE products.id IN (SELECT id FROM products WHERE name LIKE %?1% OR description LIKE %?1%)", nativeQuery = true)
    Page<ProductEntity> getProductBySearchQuery(String search, Pageable pageable);
    @Query(value = "SELECT * FROM products WHERE category_id = ?1", nativeQuery = true)
    Page<ProductEntity> getProductByCategory(Long categoryId, Pageable pageable);
    List<ProductEntity> findAllByIdIn(List<Long> ids);
}
