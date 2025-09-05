package com.convinceai.recommendationservice.client;

import com.convinceai.recommendationservice.dto.PersuasionRequest;
import com.convinceai.recommendationservice.dto.PersuasionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
public class PersuasionClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String persuasionUrl;

    public PersuasionClient(@Value("${service.persuasion.url}") String persuasionUrl) {
        this.persuasionUrl = persuasionUrl;
    }

    public Optional<PersuasionResponse> getPersuasion(PersuasionRequest requestBody) {
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(persuasionUrl + "/persuade"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                PersuasionResponse persuasionResponse = objectMapper.readValue(response.body(), PersuasionResponse.class);
                return Optional.of(persuasionResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}