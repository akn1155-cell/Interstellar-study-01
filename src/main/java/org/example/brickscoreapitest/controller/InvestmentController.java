package org.example.brickscoreapitest.controller;

import lombok.RequiredArgsConstructor;
import org.example.brickscoreapitest.domain.Investment;
import org.example.brickscoreapitest.domain.Product;
import org.example.brickscoreapitest.dto.InvestRequest;
import org.example.brickscoreapitest.service.InvestmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 투자 관련 API Controller
 *
 * 1. 전체 투자 상품 조회
 * 2. 투자하기
 * 3. 나의 투자 상품 조회
 */
@RestController
@RequestMapping("/api")  // RESTful API 컨벤션
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    /**
     * 1. 전체 투자 상품 조회 API
     *
     * - 상품 모집 기간 내의 상품만 응답
     * - 상품 ID, 제목, 총 모집 금액, 현재 모집 금액, 투자자 수, 상태, 모집 기간
     *
     * GET /api/products
     *
     * @return 모집 기간 내 상품 목록
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = investmentService.getAvailableProducts();
        return ResponseEntity.ok(products);  // 200 OK
    }

    /**
     * 2. 투자하기 API
     *
     * - 사용자 식별값은 HTTP Header X-USER-ID로 전달
     * - 총 투자모집금액 넘어서면 Sold-out 응답
     *
     * POST /api/invest
     * Header: X-USER-ID: 123
     * Body: { "productId": 1, "amount": 100000 }
     *
     * @param userId 사용자 ID (헤더에서 추출)
     * @param request 투자 요청 (상품 ID, 偸자 금액)
     * @return 투자 결과
     */
    @PostMapping("/invest")
    public ResponseEntity<Map<String, Object>> invest(
            @RequestHeader("X-USER-ID") Long userId,  // ✅ 헤더로 받음 (과제 요구사항)
            @RequestBody InvestRequest request  // JSON 요청 본문
    ) {
        try {
            // 투자 처리
            investmentService.invest(userId, request.getProductId(), request.getAmount());

            // 성공 응답
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "투자가 완료되었습니다.");

            return ResponseEntity.ok(response);  // 200 OK

        } catch (IllegalArgumentException e) {
            // 투자 불가 (상품 없음, 기간 아님)
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);  // 400 Bad Request

        } catch (IllegalStateException e) {
            // Sold-out
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "SOLD_OUT");
            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);  // 409 Conflict
        }
    }

    /**
     * 3. 나의 투자 상품 조회 API
     *
     * - 내가 투자한 모든 상품 반환
     * - 상품 ID, 제목, 총 모집 금액, 나의 투자 금액, 투자 일시
     *
     * GET /api/my-investments
     * Header: X-USER-ID: 123
     *
     * @param userId 사용자 ID (헤더에서 추출)
     * @return 나의 투자 목록
     */
    @GetMapping("/my-investments")
    public ResponseEntity<List<Investment>> getMyInvestments(
            @RequestHeader("X-USER-ID") Long userId  // ✅ 헤더로 받음
    ) {
        List<Investment> investments = investmentService.getMyInvestments(userId);
        return ResponseEntity.ok(investments);  // 200 OK
    }
}