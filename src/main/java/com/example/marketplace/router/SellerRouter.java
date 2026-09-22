package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.SellerController;

@Configuration
public class SellerRouter {

    @Bean
    public RouterFunction<ServerResponse> sellerRoutes(SellerController sellerController) {
        return RouterFunctions.route()
                .POST("/api/sellers/create", sellerController::createSeller)
                .GET("/api/sellers/index", sellerController::getAllSellers)
                .POST("/api/sellers/show", sellerController::getSellerById)
                .POST("/api/sellers/update", sellerController::updateSeller)
                .POST("/api/sellers/delete", sellerController::deleteSeller)
                .build();
    }
}
