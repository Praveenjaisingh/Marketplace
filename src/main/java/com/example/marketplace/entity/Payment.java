package com.example.marketplace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(value = 1, message = "is required")
    private int orderId;

    @NotBlank(message = "is required")
    @Pattern(regexp = "CARD|UPI|NETBANKING|COD|WALLET", message = "must be one of CARD, UPI, NETBANKING, COD, WALLET")
    private String paymentMethod;

    private String transactionId;

    @Positive(message = "must be greater than 0")
    private double amount;

    @NotBlank(message = "is required")
    @Pattern(regexp = "PENDING|PAID|FAILED|REFUNDED", message = "must be one of PENDING, PAID, FAILED, REFUNDED")
    private String paymentStatus;

    public Payment() {}

    public Payment(int orderId, String paymentMethod, String transactionId, double amount, String paymentStatus) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}