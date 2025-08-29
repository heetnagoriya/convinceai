package com.convinceai.inventoryservice.repository;

import com.convinceai.inventoryservice.model.InventoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

@Repository
public class InventoryRepository {

    private final DynamoDbTable<InventoryItem> inventoryTable;

    @Autowired
    public InventoryRepository(DynamoDbEnhancedClient enhancedClient) {
        this.inventoryTable = enhancedClient.table("inventory", TableSchema.fromBean(InventoryItem.class));
    }

    public InventoryItem save(InventoryItem item) {
        inventoryTable.putItem(item);
        return item;
    }

    public Optional<InventoryItem> findById(String productId) {
        Key key = Key.builder().partitionValue(productId).build();
        InventoryItem item = inventoryTable.getItem(key);
        return Optional.ofNullable(item);
    }
}