package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.OrderController;

@Configuration
public class OrderRouter {

    @Bean
    public RouterFunction<ServerResponse> orderRoutes(OrderController orderController) {
        return RouterFunctions.route()
                .POST("/api/orders/create", orderController::createOrder)
                .GET("/api/orders/index", orderController::getAllOrders)
                .POST("/api/orders/show", orderController::getOrderById)
                .POST("/api/orders/update", orderController::updateOrder)
                .POST("/api/orders/delete", orderController::deleteOrder)
                .build();
    }
}
