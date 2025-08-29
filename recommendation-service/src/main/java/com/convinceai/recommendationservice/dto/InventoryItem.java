package com.convinceai.recommendationservice.dto;

import lombok.Data;

@Data
public class InventoryItem {
    private String productId;
    private Integer quantity;
}