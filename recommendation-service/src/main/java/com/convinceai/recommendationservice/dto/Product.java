package com.convinceai.recommendationservice.dto;

import lombok.Data;

@Data
public class Product {
    private String productId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String recommendedProductId;
}