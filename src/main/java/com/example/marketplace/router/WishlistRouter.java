package com.example.marketplace.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.controller.WishlistController;

@Configuration
public class WishlistRouter {

    @Bean
    public RouterFunction<ServerResponse> wishlistRoutes(WishlistController wishlistController) {
        return RouterFunctions.route()
                .POST("/api/wishlist/create", wishlistController::createWishlist)
                .GET("/api/wishlist/index", wishlistController::getAllWishlists)
                .POST("/api/wishlist/show", wishlistController::getWishlistById)
                .POST("/api/wishlist/update", wishlistController::updateWishlist)
                .POST("/api/wishlist/delete", wishlistController::deleteWishlist)
                .build();
    }
}
