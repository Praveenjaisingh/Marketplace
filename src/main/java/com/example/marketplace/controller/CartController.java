package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Cart;
import com.example.marketplace.service.CartService;

@Component
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    public ServerResponse createCart(ServerRequest request) throws Exception {
        try {
            Cart cart = request.body(Cart.class);
            Cart savedCart = cartService.createCart(cart);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart created successfully",
                "data", savedCart
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllCarts(ServerRequest request) throws Exception {
        try {
            List<Cart> allCarts = cartService.getAllCarts();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Carts fetched successfully",
                "data", allCarts
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getCartById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Cart cart = cartService.getCartById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart fetched successfully",
                "data", cart
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateCart(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Cart cart = new Cart();
            cart.setUserId(((Number) body.get("userId")).intValue());
            cart.setActive((Boolean) body.get("active"));
            Cart updatedCart = cartService.updateCart(id, cart);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart updated successfully",
                "data", updatedCart
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteCart(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            cartService.deleteCart(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
