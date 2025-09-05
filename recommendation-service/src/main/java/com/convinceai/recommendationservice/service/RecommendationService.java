package com.convinceai.recommendationservice.service;

import com.convinceai.recommendationservice.client.InventoryClient;
import com.convinceai.recommendationservice.client.PersuasionClient;
import com.convinceai.recommendationservice.client.ProductCatalogClient;
import com.convinceai.recommendationservice.dto.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RecommendationService {
    private final ProductCatalogClient productCatalogClient;
    private final InventoryClient inventoryClient;
    private final PersuasionClient persuasionClient;

    public RecommendationService(ProductCatalogClient p, InventoryClient i, PersuasionClient persuasionClient) {
        this.productCatalogClient = p;
        this.inventoryClient = i;
        this.persuasionClient = persuasionClient;
    }

    public ConvinceResponse orchestrateConvinceFlow(String productId) {
        Optional<InventoryItem> inventoryOpt = inventoryClient.getInventoryByProductId(productId);
        if (inventoryOpt.isPresent() && inventoryOpt.get().getQuantity() > 0) {
            return new ConvinceResponse("stock", "Product is in stock. Added to cart.", null);
        }

        Optional<Product> originalProductOpt = productCatalogClient.getProductById(productId);
        if (originalProductOpt.isEmpty()) {
            return new ConvinceResponse("error", "Product not found.", null);
        }
        Product originalProduct = originalProductOpt.get();

        Optional<Product> recommendationOpt = findPairedRecommendation(originalProduct)
                .or(() -> findInCategoryRecommendation(originalProduct));

        if (recommendationOpt.isEmpty()) {
            return new ConvinceResponse("error", "Sorry, we couldn't find a suitable in-stock alternative right now.", null);
        }
        Product recommendation = recommendationOpt.get();

        ProductInfo originalInfo = new ProductInfo(originalProduct.getName(), originalProduct.getCategory(), originalProduct.getDescription(), originalProduct.getPrice(), 150, "Some ingredients");
        ProductInfo recommendationInfo = new ProductInfo(recommendation.getName(), recommendation.getCategory(), recommendation.getDescription(), recommendation.getPrice(), 160, "Other ingredients");
        PersuasionRequest persuasionRequest = new PersuasionRequest(originalInfo, recommendationInfo);

        String aiMessage = persuasionClient.getPersuasion(persuasionRequest)
                .map(response -> response.message())
                .orElse("Sorry, there was an issue generating a recommendation.");

        return new ConvinceResponse("recommendation", aiMessage, recommendation);
    }

    private Optional<Product> findPairedRecommendation(Product originalProduct) {
        String recommendedId = originalProduct.getRecommendedProductId();
        if (recommendedId == null || recommendedId.isEmpty()) return Optional.empty();
        return productCatalogClient.getProductById(recommendedId)
                .filter(rec -> inventoryClient.getInventoryByProductId(rec.getProductId())
                        .map(inv -> inv.getQuantity() > 0).orElse(false));
    }

    private Optional<Product> findInCategoryRecommendation(Product originalProduct) {
         return productCatalogClient.getProductsByCategory(originalProduct.getCategory()).stream()
                .filter(candidate -> !candidate.getProductId().equals(originalProduct.getProductId()))
                .filter(candidate -> inventoryClient.getInventoryByProductId(candidate.getProductId())
                        .map(inv -> inv.getQuantity() > 0).orElse(false))
                .findFirst();
    }
}