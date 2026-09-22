package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.InventoryController;

@Configuration
public class InventoryRouter {

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes(InventoryController inventoryController) {
        return RouterFunctions.route()
                .POST("/api/inventory/create", inventoryController::createInventory)
                .GET("/api/inventory/index", inventoryController::getAllInventorys)
                .POST("/api/inventory/show", inventoryController::getInventoryById)
                .POST("/api/inventory/update", inventoryController::updateInventory)
                .POST("/api/inventory/delete", inventoryController::deleteInventory)
                .build();
    }
}
