package org.example.brickscoreapitest.repository;

import org.example.brickscoreapitest.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Product DB 접근 레이어
 * JpaRepository가 CRUD 자동 제공
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStartedAtBeforeAndFinishedAtAfter(
            LocalDateTime now1,
            LocalDateTime now2
    );
}