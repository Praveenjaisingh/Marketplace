package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Wishlist;
import com.example.marketplace.service.WishlistService;

@Component
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    public ServerResponse createWishlist(ServerRequest request) throws Exception {
        try {
            Wishlist wishlist = request.body(Wishlist.class);
            Wishlist savedWishlist = wishlistService.createWishlist(wishlist);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Wishlist Item created successfully",
                "data", savedWishlist
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllWishlists(ServerRequest request) throws Exception {
        try {
            List<Wishlist> allWishlists = wishlistService.getAllWishlists();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Wishlist Items fetched successfully",
                "data", allWishlists
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getWishlistById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Wishlist wishlist = wishlistService.getWishlistById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Wishlist Item fetched successfully",
                "data", wishlist
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateWishlist(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Wishlist wishlist = new Wishlist();
            wishlist.setUserId(((Number) body.get("userId")).intValue());
            wishlist.setProductId(((Number) body.get("productId")).intValue());
            Wishlist updatedWishlist = wishlistService.updateWishlist(id, wishlist);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Wishlist Item updated successfully",
                "data", updatedWishlist
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteWishlist(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            wishlistService.deleteWishlist(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Wishlist Item deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
