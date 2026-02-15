package org.example.brickscoreapitest.service;

import lombok.RequiredArgsConstructor;
import org.example.brickscoreapitest.domain.Investment;
import org.example.brickscoreapitest.domain.Product;
import org.example.brickscoreapitest.repository.InvestmentRepository;
import org.example.brickscoreapitest.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
     *
     * @CacheEvict: 투자 후 상품 목록 캐시 삭제
     * → 다음 조회 시 최신 데이터 반환
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @CacheEvict(value = "products", allEntries = true)  // ← 캐시 삭제!
    public void invest(Long userId, Long productId, Long amount) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        if (!product.isInvestable(LocalDateTime.now())) {
            throw new IllegalArgumentException("투자 가능한 기간이 아닙니다.");
        }

        boolean success = product.invest(amount);

        if (!success) {
            throw new IllegalStateException("투자 모집 금액이 마감되었습니다.");
        }

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
     *
     * @Cacheable: Redis에 캐싱
     * - Key: "products::available"
     * - 첫 조회: DB → Redis 저장 → 반환
     * - 이후 조회: Redis에서 바로 반환 (10분간)
     */
    @Cacheable(value = "products", key = "'available'")  // ← 캐싱!
    public List<Product> getAvailableProducts() {
        System.out.println(">>> DB 조회! (캐시 미스)");  // 디버깅용
        return productRepository.findAvailableProducts(LocalDateTime.now());
    }
}