package org.example.brickscoreapitest.controller;

import lombok.RequiredArgsConstructor;
import org.example.brickscoreapitest.domain.Investment;
import org.example.brickscoreapitest.service.InvestmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 투자 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/invest")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    /**
     * 투자하기 API
     */
    @PostMapping
    public String invest(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Long amount
    ) {

        investmentService.invest(userId, productId, amount);

        return "투자 완료";
    }

    /**
     * 나의 투자 상품 조회 API
     */
    @GetMapping("/{userId}")
    public List<Investment> myInvestments(@PathVariable Long userId) {

        return investmentService.myInvestments(userId);
    }
}