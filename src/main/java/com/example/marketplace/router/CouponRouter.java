package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.CouponController;

@Configuration
public class CouponRouter {

    @Bean
    public RouterFunction<ServerResponse> couponRoutes(CouponController couponController) {
        return RouterFunctions.route()
                .POST("/api/coupons/create", couponController::createCoupon)
                .GET("/api/coupons/index", couponController::getAllCoupons)
                .POST("/api/coupons/show", couponController::getCouponById)
                .POST("/api/coupons/update", couponController::updateCoupon)
                .POST("/api/coupons/delete", couponController::deleteCoupon)
                .build();
    }
}
