package com.convinceai.inventoryservice.controller;

import com.convinceai.inventoryservice.model.InventoryItem;
import com.convinceai.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping("/{productId}")
    public InventoryItem getInventoryByProductId(@PathVariable String productId) {
        // Find the item, or if it doesn't exist, return a new one with quantity 0.
        return inventoryRepository.findById(productId)
                .orElse(createDefaultInventoryItem(productId));
    }

    @PostMapping
    public InventoryItem updateInventory(@RequestBody InventoryItem inventoryItem) {
        // This endpoint allows an admin to set or update the stock count.
        return inventoryRepository.save(inventoryItem);
    }

    private InventoryItem createDefaultInventoryItem(String productId) {
        InventoryItem defaultItem = new InventoryItem();
        defaultItem.setProductId(productId);
        defaultItem.setQuantity(0);
        return defaultItem;
    }
}