package org.example.brickscoreapitest.service;

import lombok.RequiredArgsConstructor;
import org.example.brickscoreapitest.domain.Investment;
import org.example.brickscoreapitest.domain.Product;
import org.example.brickscoreapitest.repository.InvestmentRepository;
import org.example.brickscoreapitest.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvestmentService {

    private final ProductRepository productRepository;
    private final InvestmentRepository investmentRepository;

    public void invest(Long userId, Long productId, Long amount) {

        Product product = productRepository.findById(productId).orElseThrow();

        boolean success = product.invest(amount);

        if (!success) {
            throw new RuntimeException("Sold Out");
        }

        Investment investment = Investment.create(userId, product, amount);

        investmentRepository.save(investment);
    }

    public List<Investment> myInvestments(Long userId) {
        return investmentRepository.findByUserId(userId);
    }
}