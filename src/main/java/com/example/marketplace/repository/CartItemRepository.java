package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
