package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Payment;
import com.example.marketplace.service.PaymentService;

@Component
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public ServerResponse createPayment(ServerRequest request) throws Exception {
        try {
            Payment payment = request.body(Payment.class);
            Payment savedPayment = paymentService.createPayment(payment);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Payment created successfully",
                "data", savedPayment
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllPayments(ServerRequest request) throws Exception {
        try {
            List<Payment> allPayments = paymentService.getAllPayments();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Payments fetched successfully",
                "data", allPayments
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getPaymentById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Payment payment = paymentService.getPaymentById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Payment fetched successfully",
                "data", payment
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updatePayment(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Payment payment = new Payment();
            payment.setOrderId(((Number) body.get("orderId")).intValue());
            payment.setPaymentMethod((String) body.get("paymentMethod"));
            payment.setTransactionId((String) body.get("transactionId"));
            payment.setAmount(((Number) body.get("amount")).doubleValue());
            payment.setPaymentStatus((String) body.get("paymentStatus"));
            Payment updatedPayment = paymentService.updatePayment(id, payment);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Payment updated successfully",
                "data", updatedPayment
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deletePayment(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            paymentService.deletePayment(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Payment deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
