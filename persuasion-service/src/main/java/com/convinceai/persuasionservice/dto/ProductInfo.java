package com.convinceai.persuasionservice.dto;

// Add new fields for more detailed prompts
public record ProductInfo(String name, String category, String description, double price, int calories, String ingredients) {
}