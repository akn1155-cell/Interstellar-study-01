package org.example.brickscoreapitest.repository;

import org.example.brickscoreapitest.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Investment DB 접근 레이어
 */
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    // 특정 유저 투자 목록 조회
    List<Investment> findByUserId(Long userId);
}