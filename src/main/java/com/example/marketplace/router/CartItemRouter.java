package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.CartItemController;

@Configuration
public class CartItemRouter {

    @Bean
    public RouterFunction<ServerResponse> cartItemRoutes(CartItemController cartItemController) {
        return RouterFunctions.route()
                .POST("/api/cart-items/create", cartItemController::createCartItem)
                .GET("/api/cart-items/index", cartItemController::getAllCartItems)
                .POST("/api/cart-items/show", cartItemController::getCartItemById)
                .POST("/api/cart-items/update", cartItemController::updateCartItem)
                .POST("/api/cart-items/delete", cartItemController::deleteCartItem)
                .build();
    }
}
