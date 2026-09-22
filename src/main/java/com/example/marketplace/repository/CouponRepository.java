package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

}
