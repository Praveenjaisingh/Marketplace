package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Seller;
import com.example.marketplace.repository.SellerRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;

    public SellerService(SellerRepository sellerRepository, UserRepository userRepository, ValidationUtil validationUtil) {
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
    }

    private void validateForeignKeys(Seller seller) {
        if (!userRepository.existsById(seller.getUserId())) {
            validationUtil.fail("userId", "references a user that does not exist");
        }
    }

    public Seller createSeller(Seller seller) {
        validationUtil.validate(seller);
        validateForeignKeys(seller);
        return sellerRepository.save(seller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(int id) {
        return sellerRepository.findById(id).orElseThrow(() -> new RuntimeException("Seller not found"));
    }

    public Seller updateSeller(int id, Seller sellerDetails) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new RuntimeException("Seller not found"));
        seller.setUserId(sellerDetails.getUserId());
        seller.setShopName(sellerDetails.getShopName());
        seller.setGstNumber(sellerDetails.getGstNumber());
        seller.setDescription(sellerDetails.getDescription());
        seller.setApproved(sellerDetails.isApproved());
        seller.setActive(sellerDetails.isActive());
        validationUtil.validate(seller);
        validateForeignKeys(seller);
        return sellerRepository.save(seller);
    }

    public void deleteSeller(int id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new RuntimeException("Seller not found"));
        sellerRepository.delete(seller);
    }
}
