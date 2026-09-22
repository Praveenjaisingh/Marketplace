package com.example.marketplace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(value = 1, message = "is required")
    private int productId;

    @Min(value = 0, message = "cannot be negative")
    private int stockQuantity;

    @Min(value = 0, message = "cannot be negative")
    private int reservedQuantity;

    @Size(max = 150, message = "must be at most 150 characters")
    private String warehouseLocation;

    public Inventory() {}

    public Inventory(int productId, int stockQuantity, int reservedQuantity, String warehouseLocation) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.reservedQuantity = reservedQuantity;
        this.warehouseLocation = warehouseLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

}