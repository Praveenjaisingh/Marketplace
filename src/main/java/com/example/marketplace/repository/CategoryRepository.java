package com.example.marketplace.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.marketplace.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCategoryName(String categoryName);
    Optional<Category> findByCategoryName(String categoryName);
}

