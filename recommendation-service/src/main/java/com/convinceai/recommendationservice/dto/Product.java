package com.convinceai.recommendationservice.dto;

import lombok.Data;

@Data // A Lombok shortcut for @Getter, @Setter, and more
public class Product {
    private String productId;
    private String name;
    private String description;
    private Double price;
    private String category; // We'll need this for our recommendation logic
}