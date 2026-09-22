package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Seller;
import com.example.marketplace.service.SellerService;

@Component
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    public ServerResponse createSeller(ServerRequest request) throws Exception {
        try {
            Seller seller = request.body(Seller.class);
            Seller savedSeller = sellerService.createSeller(seller);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Seller created successfully",
                "data", savedSeller
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllSellers(ServerRequest request) throws Exception {
        try {
            List<Seller> allSellers = sellerService.getAllSellers();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Sellers fetched successfully",
                "data", allSellers
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getSellerById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Seller seller = sellerService.getSellerById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Seller fetched successfully",
                "data", seller
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateSeller(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Seller seller = new Seller();
            seller.setUserId(((Number) body.get("userId")).intValue());
            seller.setShopName((String) body.get("shopName"));
            seller.setGstNumber((String) body.get("gstNumber"));
            seller.setDescription((String) body.get("description"));
            seller.setApproved((Boolean) body.get("approved"));
            seller.setActive((Boolean) body.get("active"));
            Seller updatedSeller = sellerService.updateSeller(id, seller);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Seller updated successfully",
                "data", updatedSeller
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteSeller(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            sellerService.deleteSeller(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Seller deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
