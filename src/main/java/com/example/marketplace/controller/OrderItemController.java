package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.OrderItem;
import com.example.marketplace.service.OrderItemService;

@Component
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    public ServerResponse createOrderItem(ServerRequest request) throws Exception {
        try {
            OrderItem orderItem = request.body(OrderItem.class);
            OrderItem savedOrderItem = orderItemService.createOrderItem(orderItem);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order Item created successfully",
                "data", savedOrderItem
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllOrderItems(ServerRequest request) throws Exception {
        try {
            List<OrderItem> allOrderItems = orderItemService.getAllOrderItems();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order Items fetched successfully",
                "data", allOrderItems
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getOrderItemById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            OrderItem orderItem = orderItemService.getOrderItemById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order Item fetched successfully",
                "data", orderItem
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateOrderItem(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(((Number) body.get("orderId")).intValue());
            orderItem.setProductId(((Number) body.get("productId")).intValue());
            orderItem.setSellerId(((Number) body.get("sellerId")).intValue());
            orderItem.setQuantity(((Number) body.get("quantity")).intValue());
            orderItem.setPrice(((Number) body.get("price")).doubleValue());
            OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItem);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order Item updated successfully",
                "data", updatedOrderItem
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteOrderItem(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            orderItemService.deleteOrderItem(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order Item deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
