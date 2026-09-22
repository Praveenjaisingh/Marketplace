package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.ProductController;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductController productController) {
        return RouterFunctions.route()
                .POST("/api/products/create", productController::createProduct)
                .GET("/api/products/index", productController::getAllProducts)
                .POST("/api/products/show", productController::getProductById)
                .POST("/api/products/update", productController::updateProduct)
                .POST("/api/products/delete", productController::deleteProduct)
                .build();
    }
}
