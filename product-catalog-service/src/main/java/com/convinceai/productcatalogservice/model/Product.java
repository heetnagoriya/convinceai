package com.convinceai.productcatalogservice.model;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@DynamoDbBean
public class Product {

    private String productId;
    private String name;
    private String description;
    private Double price;
    private String category; // <-- NEW FIELD ADDED

    @DynamoDbPartitionKey
    public String getProductId() {
        return productId;
    }
}