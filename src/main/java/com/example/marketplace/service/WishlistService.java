package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Wishlist;
import com.example.marketplace.repository.WishlistRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository, ValidationUtil validationUtil) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
    }


    private void validateForeignKeys(Wishlist wishlist) {
        if (!userRepository.existsById(wishlist.getUserId())) {
            validationUtil.fail("userId", "references a record that does not exist");
        }
        if (!productRepository.existsById(wishlist.getProductId())) {
            validationUtil.fail("productId", "references a record that does not exist");
        }
    }
    public Wishlist createWishlist(Wishlist wishlist) {
        validationUtil.validate(wishlist);
        validateForeignKeys(wishlist);
        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> getAllWishlists() {
        return wishlistRepository.findAll();
    }

    public Wishlist getWishlistById(int id) {
        return wishlistRepository.findById(id).orElseThrow(() -> new RuntimeException("Wishlist not found"));
    }

    public Wishlist updateWishlist(int id, Wishlist wishlistDetails) {
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new RuntimeException("Wishlist not found"));
        wishlist.setUserId(wishlistDetails.getUserId());
        wishlist.setProductId(wishlistDetails.getProductId());
        validationUtil.validate(wishlist);
        validateForeignKeys(wishlist);
        return wishlistRepository.save(wishlist);
    }

    public void deleteWishlist(int id) {
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new RuntimeException("Wishlist not found"));
        wishlistRepository.delete(wishlist);
    }
}
