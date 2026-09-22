package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.CartItem;
import com.example.marketplace.repository.CartItemRepository;
import com.example.marketplace.repository.CartRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository, ValidationUtil validationUtil) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
    }


    private void validateForeignKeys(CartItem cartItem) {
        if (!cartRepository.existsById(cartItem.getCartId())) {
            validationUtil.fail("cartId", "references a record that does not exist");
        }
        if (!productRepository.existsById(cartItem.getProductId())) {
            validationUtil.fail("productId", "references a record that does not exist");
        }
    }
    public CartItem createCartItem(CartItem cartItem) {
        validationUtil.validate(cartItem);
        validateForeignKeys(cartItem);
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public CartItem getCartItemById(int id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("CartItem not found"));
    }

    public CartItem updateCartItem(int id, CartItem cartItemDetails) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItem.setCartId(cartItemDetails.getCartId());
        cartItem.setProductId(cartItemDetails.getProductId());
        cartItem.setQuantity(cartItemDetails.getQuantity());
        cartItem.setPrice(cartItemDetails.getPrice());
        validationUtil.validate(cartItem);
        validateForeignKeys(cartItem);
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(int id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItemRepository.delete(cartItem);
    }
}
