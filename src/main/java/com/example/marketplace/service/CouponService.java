package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Coupon;
import com.example.marketplace.repository.CouponRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final ValidationUtil validationUtil;

    public CouponService(CouponRepository couponRepository, ValidationUtil validationUtil) {
        this.couponRepository = couponRepository;
        this.validationUtil = validationUtil;
    }

    public Coupon createCoupon(Coupon coupon) {
        validationUtil.validate(coupon);
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponById(int id) {
        return couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public Coupon updateCoupon(int id, Coupon couponDetails) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setCode(couponDetails.getCode());
        coupon.setDiscountPercent(couponDetails.getDiscountPercent());
        coupon.setMinOrderValue(couponDetails.getMinOrderValue());
        coupon.setMaxDiscount(couponDetails.getMaxDiscount());
        coupon.setExpiryDate(couponDetails.getExpiryDate());
        coupon.setActive(couponDetails.isActive());
        validationUtil.validate(coupon);
        return couponRepository.save(coupon);
    }

    public void deleteCoupon(int id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon not found"));
        couponRepository.delete(coupon);
    }
}
