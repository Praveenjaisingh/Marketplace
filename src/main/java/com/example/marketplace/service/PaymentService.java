package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Payment;
import com.example.marketplace.repository.PaymentRepository;
import com.example.marketplace.repository.OrderRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ValidationUtil validationUtil;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, ValidationUtil validationUtil) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.validationUtil = validationUtil;
    }


    private void validateForeignKeys(Payment payment) {
        if (!orderRepository.existsById(payment.getOrderId())) {
            validationUtil.fail("orderId", "references a record that does not exist");
        }
    }
    public Payment createPayment(Payment payment) {
        validationUtil.validate(payment);
        validateForeignKeys(payment);
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(int id) {
        return paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Payment updatePayment(int id, Payment paymentDetails) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setOrderId(paymentDetails.getOrderId());
        payment.setPaymentMethod(paymentDetails.getPaymentMethod());
        payment.setTransactionId(paymentDetails.getTransactionId());
        payment.setAmount(paymentDetails.getAmount());
        payment.setPaymentStatus(paymentDetails.getPaymentStatus());
        validationUtil.validate(payment);
        validateForeignKeys(payment);
        return paymentRepository.save(payment);
    }

    public void deletePayment(int id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
        paymentRepository.delete(payment);
    }
}
