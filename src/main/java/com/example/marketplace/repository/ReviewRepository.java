package com.example.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
