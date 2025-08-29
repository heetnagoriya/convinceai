package com.convinceai.recommendationservice.client;

import com.convinceai.recommendationservice.dto.InventoryItem;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class InventoryClient {

    private final WebClient webClient;

    public InventoryClient(@Qualifier("inventoryWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<InventoryItem> getInventoryByProductId(String productId) {
        return webClient.get()
                .uri("/inventory/{productId}", productId)
                .retrieve()
                .bodyToMono(InventoryItem.class);
    }
}