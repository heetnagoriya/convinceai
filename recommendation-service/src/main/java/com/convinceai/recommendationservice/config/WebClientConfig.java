package com.convinceai.recommendationservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${service.product-catalog.url}")
    private String productCatalogUrl;

    @Value("${service.inventory.url}")
    private String inventoryUrl;

    @Value("${service.persuasion.url}") // <-- NEW
    private String persuasionUrl;

    @Bean
    @Qualifier("productCatalogWebClient")
    public WebClient productCatalogWebClient() {
        return WebClient.builder().baseUrl(productCatalogUrl).build();
    }

    @Bean
    @Qualifier("inventoryWebClient")
    public WebClient inventoryWebClient() {
        return WebClient.builder().baseUrl(inventoryUrl).build();
    }

    @Bean // <-- NEW BEAN
    @Qualifier("persuasionWebClient")
    public WebClient persuasionWebClient() {
        return WebClient.builder().baseUrl(persuasionUrl).build();
    }
}