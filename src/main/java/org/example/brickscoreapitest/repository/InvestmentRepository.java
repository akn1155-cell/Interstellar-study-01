package org.example.brickscoreapitest.repository;

import org.example.brickscoreapitest.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Investment DB 접근 Repository
 * JpaRepository 상속으로 기본 CRUD 자동 제공
 */
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    /**
     * 특정 사용자의 투자 목록 조회
     *
     * "나의 투자 상품 조회"
     *
     * 기본 방법 (간단하지만 N+1 문제 가능):
     * - Investment 조회 → Product 정보 필요 시 추가 쿼리
     *
     * @param userId 사용자 ID
     * @return 사용자의 투자 목록 (투자일시 최신순)
     */
    List<Investment> findByUserIdOrderByInvestedAtDesc(Long userId);

    /**
     * 특정 사용자의 투자 목록 조회 (Fetch Join 최적화)
     *
     * N+1 문제 해결:
     * - Investment + Product를 한 번의 쿼리로 조회
     * - 성능 향상
     *
     * @param userId 사용자 ID
     * @return 사용자의 투자 목록 (Product 정보 포함)
     */
    @Query("SELECT i FROM Investment i " +
            "JOIN FETCH i.product " +  // Product 정보 함께 조회 (N+1 방지)
            "WHERE i.userId = :userId " +
            "ORDER BY i.investedAt DESC")  // 최신 투자 순 정렬
    List<Investment> findByUserIdWithProduct(@Param("userId") Long userId);
}