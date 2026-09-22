package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.UserController;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserController userController) {
        return RouterFunctions.route()
                .POST("/api/users/create", userController::createUser)
                .GET("/api/users/index", userController::getAllUsers)
                .POST("/api/users/show", userController::getUserById)
                .POST("/api/users/update", userController::updateUser)
                .POST("/api/users/delete", userController::deleteUser)
                .build();
    }
}
