package org.example.brickscoreapitest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Long amount;

    private LocalDateTime investedAt = LocalDateTime.now();

    public Investment(Long userId, Product product, Long amount) {
        this.userId = userId;
        this.product = product;
        this.amount = amount;
    }
}