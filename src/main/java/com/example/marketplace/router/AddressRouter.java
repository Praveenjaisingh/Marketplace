package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.AddressController;

@Configuration
public class AddressRouter {

    @Bean
    public RouterFunction<ServerResponse> addressRoutes(AddressController addressController) {
        return RouterFunctions.route()
                .POST("/api/addresses/create", addressController::createAddress)
                .GET("/api/addresses/index", addressController::getAllAddresss)
                .POST("/api/addresses/show", addressController::getAddressById)
                .POST("/api/addresses/update", addressController::updateAddress)
                .POST("/api/addresses/delete", addressController::deleteAddress)
                .build();
    }
}
