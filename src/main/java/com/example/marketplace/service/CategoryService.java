package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Category;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ValidationUtil validationUtil;

    public CategoryService(CategoryRepository categoryRepository, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.validationUtil = validationUtil;
    }

    public Category createCategory(Category category) {
        validationUtil.validate(category);
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategorys() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category updateCategory(int id, Category categoryDetails) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryName(categoryDetails.getCategoryName());
        category.setDescription(categoryDetails.getDescription());
        category.setActive(categoryDetails.isActive());
        validationUtil.validate(category);
        return categoryRepository.save(category);
    }

    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}
