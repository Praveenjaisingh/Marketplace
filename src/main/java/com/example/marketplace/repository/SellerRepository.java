package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {

}
