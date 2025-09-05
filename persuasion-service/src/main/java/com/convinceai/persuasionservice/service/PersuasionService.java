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

    // --- THIS IS THE NEW, MORE DETAILED PROMPT ---
    private final String promptTemplate = """
            You are a witty and persuasive e-commerce salesperson. A customer wanted to buy "%s" (priced at ₹%.2f), but it's out of stock.
            Your task is to passionately convince them to buy "%s" (priced at ₹%.2f) instead.

            Here is the information you must use for the recommended product:
            - Name: %s
            - Description: %s
            - Ingredients: %s

            You MUST generate a response that is exciting, uses bullet points, and follows this structure:
            1.  Acknowledge the out-of-stock item and introduce the alternative with excitement (use an emoji!).
            2.  Create a bulleted list of 3-4 points highlighting the benefits. Mention the great price.
            3.  Add a personal, trustworthy closing statement like "Trust me on this one...".
            4.  End with a strong call to action.
            """;

    public PersuasionService(WebClient.Builder webClientBuilder, @Value("${google.api.key}") String apiKey) {
        this.webClient = webClientBuilder.build();
        this.apiKey = apiKey;
    }

    public PersuasionResponse getPersuasionMessage(PersuasionRequest request) {
        String finalPrompt = String.format(promptTemplate,
            request.originalProduct().name(),
            request.originalProduct().price(),
            request.recommendedProduct().name(),
            request.recommendedProduct().price(),
            request.recommendedProduct().name(),
            request.recommendedProduct().description(),
            request.recommendedProduct().ingredients()
        );

        // ... (rest of the method is the same)
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