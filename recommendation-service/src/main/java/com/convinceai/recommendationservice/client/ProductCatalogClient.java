package com.convinceai.recommendationservice.client;

import com.convinceai.recommendationservice.dto.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Component
public class ProductCatalogClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String productCatalogUrl;

    public ProductCatalogClient(@Value("${service.product-catalog.url}") String productCatalogUrl) {
        this.productCatalogUrl = productCatalogUrl;
    }

    public Optional<Product> getProductById(String productId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(productCatalogUrl + "/products/" + productId))
                    .GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Product product = objectMapper.readValue(response.body(), Product.class);
                return Optional.of(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Product> getProductsByCategory(String category) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(productCatalogUrl + "/products?category=" + category))
                    .GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<List<Product>>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
}