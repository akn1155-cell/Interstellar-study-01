package org.example.brickscoreapitest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 투자 요청 DTO
 */
@Getter
@NoArgsConstructor
public class InvestRequest {

    private Long productId;  // 투자할 상품 ID
    private Long amount;     // 투자 금액

    public InvestRequest(Long productId, Long amount) {
        this.productId = productId;
        this.amount = amount;
    }
}