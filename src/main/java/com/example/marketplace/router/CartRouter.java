package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.CartController;

@Configuration
public class CartRouter {

    @Bean
    public RouterFunction<ServerResponse> cartRoutes(CartController cartController) {
        return RouterFunctions.route()
                .POST("/api/carts/create", cartController::createCart)
                .GET("/api/carts/index", cartController::getAllCarts)
                .POST("/api/carts/show", cartController::getCartById)
                .POST("/api/carts/update", cartController::updateCart)
                .POST("/api/carts/delete", cartController::deleteCart)
                .build();
    }
}
