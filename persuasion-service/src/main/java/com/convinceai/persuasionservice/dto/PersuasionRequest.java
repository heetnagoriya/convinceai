package com.convinceai.persuasionservice.dto;

// This defines the request we expect, containing two products
public record PersuasionRequest(ProductInfo originalProduct, ProductInfo recommendedProduct) {
}