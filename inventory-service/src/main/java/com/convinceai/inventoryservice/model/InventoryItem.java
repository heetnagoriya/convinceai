package com.convinceai.inventoryservice.model;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@DynamoDbBean
public class InventoryItem {

    private String productId;
    private Integer quantity;

    @DynamoDbPartitionKey
    public String getProductId() {
        return productId;
    }
}