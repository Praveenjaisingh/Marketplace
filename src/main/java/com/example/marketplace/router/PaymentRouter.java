package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.PaymentController;

@Configuration
public class PaymentRouter {

    @Bean
    public RouterFunction<ServerResponse> paymentRoutes(PaymentController paymentController) {
        return RouterFunctions.route()
                .POST("/api/payments/create", paymentController::createPayment)
                .GET("/api/payments/index", paymentController::getAllPayments)
                .POST("/api/payments/show", paymentController::getPaymentById)
                .POST("/api/payments/update", paymentController::updatePayment)
                .POST("/api/payments/delete", paymentController::deletePayment)
                .build();
    }
}
