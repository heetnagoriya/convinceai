package com.convinceai.recommendationservice.client;

import com.convinceai.recommendationservice.dto.PersuasionRequest;
import com.convinceai.recommendationservice.dto.PersuasionResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PersuasionClient {

    private final WebClient webClient;

    public PersuasionClient(@Qualifier("persuasionWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PersuasionResponse> getPersuasion(PersuasionRequest request) {
        return webClient.post()
                .uri("/persuade")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PersuasionResponse.class);
    }
}