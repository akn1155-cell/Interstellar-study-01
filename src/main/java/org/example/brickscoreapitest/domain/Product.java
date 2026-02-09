package org.example.brickscoreapitest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long totalAmount;

    private Long currentAmount = 0L;

    private Integer investorCount = 0;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    public boolean invest(Long amount) {
        if (currentAmount + amount > totalAmount) {
            return false;
        }

        currentAmount += amount;
        investorCount++;

        return true;
    }
}
