package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.OrderItemController;

@Configuration
public class OrderItemRouter {

    @Bean
    public RouterFunction<ServerResponse> orderItemRoutes(OrderItemController orderItemController) {
        return RouterFunctions.route()
                .POST("/api/order-items/create", orderItemController::createOrderItem)
                .GET("/api/order-items/index", orderItemController::getAllOrderItems)
                .POST("/api/order-items/show", orderItemController::getOrderItemById)
                .POST("/api/order-items/update", orderItemController::updateOrderItem)
                .POST("/api/order-items/delete", orderItemController::deleteOrderItem)
                .build();
    }
}
