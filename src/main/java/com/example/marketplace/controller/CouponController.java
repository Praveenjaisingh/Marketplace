package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Coupon;
import com.example.marketplace.service.CouponService;

@Component
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    public ServerResponse createCoupon(ServerRequest request) throws Exception {
        try {
            Coupon coupon = request.body(Coupon.class);
            Coupon savedCoupon = couponService.createCoupon(coupon);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Coupon created successfully",
                "data", savedCoupon
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllCoupons(ServerRequest request) throws Exception {
        try {
            List<Coupon> allCoupons = couponService.getAllCoupons();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Coupons fetched successfully",
                "data", allCoupons
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getCouponById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Coupon coupon = couponService.getCouponById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Coupon fetched successfully",
                "data", coupon
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateCoupon(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Coupon coupon = new Coupon();
            coupon.setCode((String) body.get("code"));
            coupon.setDiscountPercent(((Number) body.get("discountPercent")).doubleValue());
            coupon.setMinOrderValue(((Number) body.get("minOrderValue")).doubleValue());
            coupon.setMaxDiscount(((Number) body.get("maxDiscount")).doubleValue());
            coupon.setExpiryDate((String) body.get("expiryDate"));
            coupon.setActive((Boolean) body.get("active"));
            Coupon updatedCoupon = couponService.updateCoupon(id, coupon);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Coupon updated successfully",
                "data", updatedCoupon
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteCoupon(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            couponService.deleteCoupon(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Coupon deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
