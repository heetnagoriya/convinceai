package com.convinceai.persuasionservice.service;

import com.convinceai.persuasionservice.dto.PersuasionRequest;
import com.convinceai.persuasionservice.dto.PersuasionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PersuasionService {

    private final WebClient webClient;
    private final String apiKey;
    private final String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    // THIS IS OUR UPGRADED PROMPT USING JAVA'S BUILT-IN FORMATTING
    private final String promptTemplate = """
            You are a world-class, enthusiastic e-commerce salesperson with a personal touch.
            A customer wanted to buy "%s", but it's out of stock.
            Your goal is to passionately convince them to buy "%s" instead.

            Here are the details:
            - Original Product: %s (%s). Description: "%s".
            - Recommended Product: %s (%s). Description: "%s".
            - Recommended Product Details: It has %d calories and is made from %s. Its price is %.2f.

            Craft a persuasive message with the following structure:
            1.  Acknowledge the out-of-stock item with empathy and introduce the alternative with excitement (use an emoji!).
            2.  Describe the key similarities and unique benefits of the recommended product, using the specific details provided. Mention the great value.
            3.  Add a personal, trustworthy closing statement (e.g., "Trust me on this one...").
            4.  End with a strong, friendly call to action to add the item to their cart.
            """;


    public PersuasionService(WebClient.Builder webClientBuilder, @Value("${google.api.key}") String apiKey) {
        this.webClient = webClientBuilder.build();
        this.apiKey = apiKey;
    }

    public PersuasionResponse getPersuasionMessage(PersuasionRequest request) {
        
        // We use String.format() to build the final prompt. No external library needed.
        String finalPrompt = String.format(promptTemplate,
            request.originalProduct().name(),
            request.recommendedProduct().name(),
            request.originalProduct().name(),
            request.originalProduct().category(),
            request.originalProduct().description(),
            request.recommendedProduct().name(),
            request.recommendedProduct().category(),
            request.recommendedProduct().description(),
            request.recommendedProduct().calories(),
            request.recommendedProduct().ingredients(),
            request.recommendedProduct().price()
        );

        var requestBody = new GeminiRequest(List.of(new Content(List.of(new Part(finalPrompt)))));

        JsonNode response = webClient.post()
                .uri(geminiApiUrl + "?key=" + this.apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        
        String aiResponse = response.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
        
        return new PersuasionResponse(aiResponse);
    }

    private record Part(String text) {}
    private record Content(List<Part> parts) {}
    private record GeminiRequest(List<Content> contents) {}
}