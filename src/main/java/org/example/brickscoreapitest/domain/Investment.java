package org.example.brickscoreapitest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
@Getter
@NoArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // 투자자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 상품

    @Column(nullable = false)
    private Long amount; // 투자금액

    @Column(nullable = false)
    private LocalDateTime investedAt; // 투자시간

    // 생성자
    private Investment(Long userId, Product product, Long amount) {
        this.userId = userId;
        this.product = product;
        this.amount = amount;
        this.investedAt = LocalDateTime.now();
    }

    // 정적 팩토리
    public static Investment create(Long userId, Product product, Long amount) {
        return new Investment(userId, product, amount);
    }
}