package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
