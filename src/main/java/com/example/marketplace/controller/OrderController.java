package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Order;
import com.example.marketplace.service.OrderService;

@Component
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public ServerResponse createOrder(ServerRequest request) throws Exception {
        try {
            Order order = request.body(Order.class);
            Order savedOrder = orderService.createOrder(order);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order created successfully",
                "data", savedOrder
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllOrders(ServerRequest request) throws Exception {
        try {
            List<Order> allOrders = orderService.getAllOrders();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Orders fetched successfully",
                "data", allOrders
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getOrderById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Order order = orderService.getOrderById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order fetched successfully",
                "data", order
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateOrder(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Order order = new Order();
            order.setUserId(((Number) body.get("userId")).intValue());
            order.setAddressId(((Number) body.get("addressId")).intValue());
            order.setCouponCode((String) body.get("couponCode"));
            order.setTotalAmount(((Number) body.get("totalAmount")).doubleValue());
            order.setOrderStatus((String) body.get("orderStatus"));
            order.setPaymentStatus((String) body.get("paymentStatus"));
            Order updatedOrder = orderService.updateOrder(id, order);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order updated successfully",
                "data", updatedOrder
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteOrder(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            orderService.deleteOrder(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Order deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
