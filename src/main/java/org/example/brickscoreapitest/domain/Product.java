package org.example.brickscoreapitest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

/**
 * 투자 상품 Entity
 * 실제 DB의 product 테이블과 매핑됨
 */
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동증가
    private Long id;

    @Column(nullable = false)
    private String title; // 상품 제목

    @Column(name = "total_investing_amount", nullable = false)
    private Long totalInvestingAmount; // 총투자금액

    @Column(name = "current_investing_amount", nullable = false)
    private Long currentInvestingAmount = 0L; // 현재투자금액

    @Column(name = "investor_count", nullable = false)
    private Integer investorCount = 0; // 투자자수

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.OPEN; // 투자모집상태

    @Version
    private Long version; // 동시성 처리를 위한 변수

    // 생성자
    public Product(String title, Long totalInvestingAmount,
                   LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.title = title;
        this.totalInvestingAmount = totalInvestingAmount;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }
    /**
     * 투자 가능한 상품인지 확인
     * "상품 모집 기간 내의 상품만 응답"
     */
    public boolean isInvestable(LocalDateTime now) {
        return status == ProductStatus.OPEN
                && !now.isBefore(startedAt)
                && !now.isAfter(finishedAt);
    }

    /**
     * 투자 처리
     * - 총 모집금액 초과 시 false
     * - 누적 모집 금액, 투자자 수 증가
     * - Sold-out 자동 처리
     */
    public boolean invest(Long amount) {
        // 모집금액 초과 체크
        if (currentInvestingAmount + amount > totalInvestingAmount) {
            return false;
        }

        // 투자 처리
        this.currentInvestingAmount += amount;
        this.investorCount += 1;

        // Sold-out 체크
        if (this.currentInvestingAmount >= this.totalInvestingAmount) {
            this.status = ProductStatus.SOLD_OUT;
        }

        return true;
    }
}
