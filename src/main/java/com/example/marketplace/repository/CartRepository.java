package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}
