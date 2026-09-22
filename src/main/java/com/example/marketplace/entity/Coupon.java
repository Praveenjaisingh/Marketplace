package com.example.marketplace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "is required")
    @Pattern(regexp = "^[A-Z0-9]{3,20}$", message = "must be 3-20 uppercase letters/digits")
    private String code;

    @DecimalMin(value = "0.0", inclusive = false, message = "must be greater than 0")
    @DecimalMax(value = "100.0", message = "cannot exceed 100")
    private double discountPercent;

    @DecimalMin(value = "0.0", message = "cannot be negative")
    private double minOrderValue;

    @DecimalMin(value = "0.0", message = "cannot be negative")
    private double maxDiscount;

    @NotBlank(message = "is required")
    private String expiryDate;

    private boolean active;

    public Coupon() {}

    public Coupon(String code, double discountPercent, double minOrderValue, double maxDiscount, String expiryDate, boolean active) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.minOrderValue = minOrderValue;
        this.maxDiscount = maxDiscount;
        this.expiryDate = expiryDate;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}