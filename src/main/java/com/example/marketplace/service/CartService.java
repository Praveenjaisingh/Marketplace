package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Cart;
import com.example.marketplace.repository.CartRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ValidationUtil validationUtil) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
    }


    private void validateForeignKeys(Cart cart) {
        if (!userRepository.existsById(cart.getUserId())) {
            validationUtil.fail("userId", "references a record that does not exist");
        }
    }
    public Cart createCart(Cart cart) {
        validationUtil.validate(cart);
        validateForeignKeys(cart);
        return cartRepository.save(cart);
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Cart getCartById(int id) {
        return cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart updateCart(int id, Cart cartDetails) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setUserId(cartDetails.getUserId());
        cart.setActive(cartDetails.isActive());
        validationUtil.validate(cart);
        validateForeignKeys(cart);
        return cartRepository.save(cart);
    }

    public void deleteCart(int id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }
}
