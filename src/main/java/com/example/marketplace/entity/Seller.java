package com.example.marketplace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(value = 1, message = "is required")
    private int userId;

    @NotBlank(message = "is required")
    @Size(max = 100, message = "must be at most 100 characters")
    private String shopName;

    @Pattern(regexp = "^$|^[0-9A-Z]{15}$", message = "must be a valid 15-character GST number")
    private String gstNumber;

    @Size(max = 1000, message = "must be at most 1000 characters")
    private String description;

    private boolean approved;
    private boolean active;

    public Seller() {}

    public Seller(int userId, String shopName, String gstNumber, String description, boolean approved, boolean active) {
        this.userId = userId;
        this.shopName = shopName;
        this.gstNumber = gstNumber;
        this.description = description;
        this.approved = approved;
        this.active = active;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}