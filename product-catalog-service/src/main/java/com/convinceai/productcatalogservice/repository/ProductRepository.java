package com.convinceai.productcatalogservice.repository;

import com.convinceai.productcatalogservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final DynamoDbTable<Product> productTable;

    @Autowired
    public ProductRepository(DynamoDbEnhancedClient enhancedClient) {
        this.productTable = enhancedClient.table("products", TableSchema.fromBean(Product.class));
    }

    public Product save(Product product) {
        productTable.putItem(product);
        return product;
    }

    public Optional<Product> findById(String productId) {
        Key key = Key.builder().partitionValue(productId).build();
        return Optional.ofNullable(productTable.getItem(key));
    }

    // NEW METHOD ADDED
    public List<Product> findByCategory(String category) {
        // This performs a scan on the table, which is fine for our small dataset.
        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .filterExpression(software.amazon.awssdk.enhanced.dynamodb.Expression.builder()
                        .expression("category = :val")
                        .expressionValues(Map.of(":val", AttributeValue.builder().s(category).build()))
                        .build())
                .build();

        return productTable.scan(request).items().stream().collect(Collectors.toList());
    }
}