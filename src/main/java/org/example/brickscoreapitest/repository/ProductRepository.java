package org.example.brickscoreapitest.repository;

import org.example.brickscoreapitest.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Product DB 접근 Repository
 * JpaRepository 상속으로 기본 CRUD 자동 제공
 * - save(), findById(), findAll(), delete() 등
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 모집 기간 내의 상품만 조회
     *
     * "상품 모집 기간 내의 상품만 응답"
     *
     * @param now 현재 시각
     * @return 모집 기간 내 상품 목록
     */
    @Query("SELECT p FROM Product p " +
            "WHERE p.startedAt <= :now " +  // 시작일이 현재보다 이전
            "AND p.finishedAt >= :now")     // 종료일이 현재보다 이후
    List<Product> findAvailableProducts(@Param("now") LocalDateTime now);

    /**
     * 투자 시 비관적 락 적용하여 상품 조회
     *
     * 동시성 처리 강화:
     * - @Version (Optimistic Lock) 기본 사용
     * - Pessimistic Lock 추가 옵션
     *
     * @param id 상품 ID
     * @return 상품 (락 걸린 상태)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)  // 비관적 락 (선택적 사용)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);
}