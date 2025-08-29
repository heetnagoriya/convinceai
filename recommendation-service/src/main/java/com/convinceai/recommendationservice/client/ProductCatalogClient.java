package com.convinceai.recommendationservice.client;

import com.convinceai.recommendationservice.dto.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductCatalogClient {

    private final WebClient webClient;

    public ProductCatalogClient(@Qualifier("productCatalogWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Product> getProductById(String productId) {
        return webClient.get()
                .uri("/products/{productId}", productId)
                .retrieve()
                .bodyToMono(Product.class);
    }

    // THIS IS THE NEW METHOD THAT WAS MISSING
    public Flux<Product> getProductsByCategory(String category) {
        // This endpoint doesn't exist yet in our product-catalog-service.
        // We will build it in the next step!
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/products")
                        .queryParam("category", category)
                        .build())
                .retrieve()
                .bodyToFlux(Product.class);
    }
}