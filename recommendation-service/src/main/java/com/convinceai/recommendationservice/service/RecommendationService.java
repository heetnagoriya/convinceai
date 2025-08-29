package com.convinceai.recommendationservice.service;

import com.convinceai.recommendationservice.client.InventoryClient;
import com.convinceai.recommendationservice.client.PersuasionClient;
import com.convinceai.recommendationservice.client.ProductCatalogClient;
import com.convinceai.recommendationservice.dto.Product;
import com.convinceai.recommendationservice.dto.ProductInfo;
import com.convinceai.recommendationservice.dto.PersuasionRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RecommendationService {

    private final ProductCatalogClient productCatalogClient;
    private final InventoryClient inventoryClient;
    private final PersuasionClient persuasionClient; // <-- NEW

    public RecommendationService(ProductCatalogClient productCatalogClient, InventoryClient inventoryClient, PersuasionClient persuasionClient) {
        this.productCatalogClient = productCatalogClient;
        this.inventoryClient = inventoryClient;
        this.persuasionClient = persuasionClient; // <-- NEW
    }

    // THIS IS OUR NEW MASTER METHOD
    public Mono<String> orchestrateConvinceFlow(String productId) {
        // Step 1: Check inventory
        return inventoryClient.getInventoryByProductId(productId)
                .flatMap(inventoryItem -> {
                    // Step 2: If item is in stock, return a simple success message.
                    if (inventoryItem.getQuantity() > 0) {
                        return Mono.just("Product is in stock. Added to cart.");
                    }
                    
                    // Step 3: If out of stock, start the ConvinceAI flow
                    return findRecommendationAndPersuade(productId);
                });
    }

    private Mono<String> findRecommendationAndPersuade(String outOfStockProductId) {
        // Get details of the out-of-stock product
        Mono<Product> originalProductMono = productCatalogClient.getProductById(outOfStockProductId).cache();

        // Find an in-stock recommendation
        Mono<Product> recommendationMono = originalProductMono
                .flatMap(outOfStockProduct -> 
                    productCatalogClient.getProductsByCategory(outOfStockProduct.getCategory())
                        .filter(candidate -> !candidate.getProductId().equals(outOfStockProductId))
                        .next() // Get the first alternative
                        .flatMap(this::checkInventory)
                );

        // When we have both products, call the persuasion service
        return Mono.zip(originalProductMono, recommendationMono)
                .flatMap(tuple -> {
                    Product original = tuple.getT1();
                    Product recommendation = tuple.getT2();
                    
                    // Create the DTOs needed by the persuasion service
                    ProductInfo originalInfo = new ProductInfo(original.getName(), original.getCategory(), original.getDescription(), original.getPrice(), 0, ""); // Dummy values for now
                    ProductInfo recommendationInfo = new ProductInfo(recommendation.getName(), recommendation.getCategory(), recommendation.getDescription(), recommendation.getPrice(), 0, ""); // Dummy values for now

                    PersuasionRequest persuasionRequest = new PersuasionRequest(originalInfo, recommendationInfo);
                    
                    return persuasionClient.getPersuasion(persuasionRequest)
                            .map(persuasionResponse -> persuasionResponse.message()); // Return just the AI's message
                })
                .defaultIfEmpty("Sorry, we couldn't find a suitable in-stock alternative right now.");
    }

    private Mono<Product> checkInventory(Product candidate) {
        return inventoryClient.getInventoryByProductId(candidate.getProductId())
                .filter(inventoryItem -> inventoryItem.getQuantity() > 0)
                .map(inventoryItem -> candidate);
    }
}