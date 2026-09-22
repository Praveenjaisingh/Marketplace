package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
