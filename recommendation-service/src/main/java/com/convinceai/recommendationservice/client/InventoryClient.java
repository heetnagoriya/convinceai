package com.convinceai.recommendationservice.client;

import com.convinceai.recommendationservice.dto.InventoryItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
public class InventoryClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String inventoryUrl;

    public InventoryClient(@Value("${service.inventory.url}") String inventoryUrl) {
        this.inventoryUrl = inventoryUrl;
    }

    public Optional<InventoryItem> getInventoryByProductId(String productId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(inventoryUrl + "/inventory/" + productId))
                    .GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                InventoryItem item = objectMapper.readValue(response.body(), InventoryItem.class);
                return Optional.of(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}