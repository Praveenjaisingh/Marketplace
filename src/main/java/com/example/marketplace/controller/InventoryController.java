package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Inventory;
import com.example.marketplace.service.InventoryService;

@Component
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ServerResponse createInventory(ServerRequest request) throws Exception {
        try {
            Inventory inventory = request.body(Inventory.class);
            Inventory savedInventory = inventoryService.createInventory(inventory);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Inventory created successfully",
                "data", savedInventory
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllInventorys(ServerRequest request) throws Exception {
        try {
            List<Inventory> allInventorys = inventoryService.getAllInventorys();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Inventorys fetched successfully",
                "data", allInventorys
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getInventoryById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Inventory inventory = inventoryService.getInventoryById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Inventory fetched successfully",
                "data", inventory
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateInventory(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Inventory inventory = new Inventory();
            inventory.setProductId(((Number) body.get("productId")).intValue());
            inventory.setStockQuantity(((Number) body.get("stockQuantity")).intValue());
            inventory.setReservedQuantity(((Number) body.get("reservedQuantity")).intValue());
            inventory.setWarehouseLocation((String) body.get("warehouseLocation"));
            Inventory updatedInventory = inventoryService.updateInventory(id, inventory);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Inventory updated successfully",
                "data", updatedInventory
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteInventory(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            inventoryService.deleteInventory(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Inventory deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
