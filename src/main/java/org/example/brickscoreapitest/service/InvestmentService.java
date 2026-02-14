package org.example.brickscoreapitest.service;

import lombok.RequiredArgsConstructor;
import org.example.brickscoreapitest.domain.Investment;
import org.example.brickscoreapitest.domain.Product;
import org.example.brickscoreapitest.repository.InvestmentRepository;
import org.example.brickscoreapitest.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvestmentService {

    private final ProductRepository productRepository;
    private final InvestmentRepository investmentRepository;

    /**
     * 투자 처리
     * @Version (Optimistic Lock)으로 동시성 제어
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    // @Retryable 관련 코드 전부 삭제!
    public void invest(Long userId, Long productId, Long amount) {

        // 1. 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 2. 투자 가능 여부 확인
        if (!product.isInvestable(LocalDateTime.now())) {
            throw new IllegalArgumentException("투자 가능한 기간이 아닙니다.");
        }

        // 3. 투자 처리
        boolean success = product.invest(amount);

        // 4. Sold-out 확인
        if (!success) {
            throw new IllegalStateException("투자 모집 금액이 마감되었습니다.");
        }

        // 5. 투자 기록 저장
        Investment investment = new Investment(userId, product, amount);
        investmentRepository.save(investment);
    }

    /**
     * 나의 투자 상품 조회
     */
    public List<Investment> getMyInvestments(Long userId) {
        return investmentRepository.findByUserIdWithProduct(userId);
    }

    /**
     * 전체 투자 상품 조회
     */
    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts(LocalDateTime.now());
    }
}