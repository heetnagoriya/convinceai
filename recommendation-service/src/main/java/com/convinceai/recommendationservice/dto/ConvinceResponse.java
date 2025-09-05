package com.convinceai.recommendationservice.dto;

// This is the new, smart "package" of information our API will send
public record ConvinceResponse(
    String type, // Will be "stock", "recommendation", or "error"
    String message,
    Product recommendedProduct // The full details of the recommended product
) {}