package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.CategoryController;

@Configuration
public class CategoryRouter {

    @Bean
    public RouterFunction<ServerResponse> categoryRoutes(CategoryController categoryController) {
        return RouterFunctions.route()
                .POST("/api/categories/create", categoryController::createCategory)
                .GET("/api/categories/index", categoryController::getAllCategorys)
                .POST("/api/categories/show", categoryController::getCategoryById)
                .POST("/api/categories/update", categoryController::updateCategory)
                .POST("/api/categories/delete", categoryController::deleteCategory)
                .build();
    }
}
