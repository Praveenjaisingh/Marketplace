package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Inventory;
import com.example.marketplace.repository.InventoryRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository, ValidationUtil validationUtil) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
    }


    private void validateForeignKeys(Inventory inventory) {
        if (!productRepository.existsById(inventory.getProductId())) {
            validationUtil.fail("productId", "references a record that does not exist");
        }
    }
    public Inventory createInventory(Inventory inventory) {
        validationUtil.validate(inventory);
        validateForeignKeys(inventory);
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getAllInventorys() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(int id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    public Inventory updateInventory(int id, Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventory.setProductId(inventoryDetails.getProductId());
        inventory.setStockQuantity(inventoryDetails.getStockQuantity());
        inventory.setReservedQuantity(inventoryDetails.getReservedQuantity());
        inventory.setWarehouseLocation(inventoryDetails.getWarehouseLocation());
        validationUtil.validate(inventory);
        validateForeignKeys(inventory);
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(int id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventoryRepository.delete(inventory);
    }
}
