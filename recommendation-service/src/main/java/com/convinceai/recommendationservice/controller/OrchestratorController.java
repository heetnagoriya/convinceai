package com.convinceai.recommendationservice.controller;

import com.convinceai.recommendationservice.service.RecommendationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class OrchestratorController {

    private final RecommendationService recommendationService;

    public OrchestratorController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/cart/add/{productId}")
    public Mono<String> addToCart(@PathVariable String productId) {
        return recommendationService.orchestrateConvinceFlow(productId);
    }
}