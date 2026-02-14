package org.example.brickscoreapitest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 투자 기록 Entity
 * 사용자의 투자 내역 저장
 */
@Entity
@Table(name = "investments",
        indexes = @Index(name = "idx_user_id", columnList = "user_id"))  // 나의 투자 조회 성능 최적화
@Getter
@NoArgsConstructor  // JPA 기본 생성자
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 투자 기록 고유 ID

    @Column(name = "user_id", nullable = false)
    private Long userId;  // 투자한 사용자 ID (X-USER-ID 헤더 값)

    @ManyToOne(fetch = FetchType.LAZY)  // N:1 관계, 지연 로딩으로 성능 최적화
    @JoinColumn(name = "product_id", nullable = false)  // FK 컬럼명 명시
    private Product product;  // 투자한 상품 (Product 객체와 연결)

    @Column(name = "investing_amount", nullable = false)  // 과제 요구사항 컬럼명
    private Long investingAmount;  // 투자 금액

    @Column(name = "invested_at", nullable = false)  // 과제 요구사항 컬럼명
    private LocalDateTime investedAt;  // 투자 일시

    /**
     * 투자 기록 생성자
     * @param userId 투자자 ID
     * @param product 투자 상품
     * @param investingAmount 투자 금액
     */
    public Investment(Long userId, Product product, Long investingAmount) {
        this.userId = userId;  // 사용자 ID 설정
        this.product = product;  // 상품 연결
        this.investingAmount = investingAmount;  // 투자 금액 설정
        this.investedAt = LocalDateTime.now();  // 현재 시각 자동 저장
    }
}