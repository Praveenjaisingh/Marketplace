package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.CartItem;
import com.example.marketplace.service.CartItemService;

@Component
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    public ServerResponse createCartItem(ServerRequest request) throws Exception {
        try {
            CartItem cartItem = request.body(CartItem.class);
            CartItem savedCartItem = cartItemService.createCartItem(cartItem);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart Item created successfully",
                "data", savedCartItem
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllCartItems(ServerRequest request) throws Exception {
        try {
            List<CartItem> allCartItems = cartItemService.getAllCartItems();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart Items fetched successfully",
                "data", allCartItems
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getCartItemById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            CartItem cartItem = cartItemService.getCartItemById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart Item fetched successfully",
                "data", cartItem
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateCartItem(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            CartItem cartItem = new CartItem();
            cartItem.setCartId(((Number) body.get("cartId")).intValue());
            cartItem.setProductId(((Number) body.get("productId")).intValue());
            cartItem.setQuantity(((Number) body.get("quantity")).intValue());
            cartItem.setPrice(((Number) body.get("price")).doubleValue());
            CartItem updatedCartItem = cartItemService.updateCartItem(id, cartItem);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart Item updated successfully",
                "data", updatedCartItem
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteCartItem(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            cartItemService.deleteCartItem(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Cart Item deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
