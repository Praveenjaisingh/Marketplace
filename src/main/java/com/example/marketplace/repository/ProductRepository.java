package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
