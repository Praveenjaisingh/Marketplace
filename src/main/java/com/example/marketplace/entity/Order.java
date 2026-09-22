package com.example.marketplace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(value = 1, message = "is required")
    private int userId;

    @Min(value = 1, message = "is required")
    private int addressId;

    private String couponCode;

    @Positive(message = "must be greater than 0")
    private double totalAmount;

    @NotBlank(message = "is required")
    @jakarta.validation.constraints.Pattern(regexp = "PLACED|CONFIRMED|SHIPPED|DELIVERED|CANCELLED", message = "must be one of PLACED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED")
    private String orderStatus;

    @NotBlank(message = "is required")
    @jakarta.validation.constraints.Pattern(regexp = "PENDING|PAID|FAILED|REFUNDED", message = "must be one of PENDING, PAID, FAILED, REFUNDED")
    private String paymentStatus;

    public Order() {}

    public Order(int userId, int addressId, String couponCode, double totalAmount, String orderStatus, String paymentStatus) {
        this.userId = userId;
        this.addressId = addressId;
        this.couponCode = couponCode;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}