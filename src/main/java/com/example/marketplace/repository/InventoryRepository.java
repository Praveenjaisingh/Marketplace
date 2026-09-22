package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

}
