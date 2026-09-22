package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

}
