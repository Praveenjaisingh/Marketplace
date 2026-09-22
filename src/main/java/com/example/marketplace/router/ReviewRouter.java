package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.ReviewController;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoutes(ReviewController reviewController) {
        return RouterFunctions.route()
                .POST("/api/reviews/create", reviewController::createReview)
                .GET("/api/reviews/index", reviewController::getAllReviews)
                .POST("/api/reviews/show", reviewController::getReviewById)
                .POST("/api/reviews/update", reviewController::updateReview)
                .POST("/api/reviews/delete", reviewController::deleteReview)
                .build();
    }
}
