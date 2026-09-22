package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
