package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Category;
import com.example.marketplace.service.CategoryService;

@Component
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ServerResponse createCategory(ServerRequest request) throws Exception {
        try {
            Category category = request.body(Category.class);
            Category savedCategory = categoryService.createCategory(category);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Category created successfully",
                "data", savedCategory
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllCategorys(ServerRequest request) throws Exception {
        try {
            List<Category> allCategorys = categoryService.getAllCategorys();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Categorys fetched successfully",
                "data", allCategorys
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getCategoryById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Category category = categoryService.getCategoryById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Category fetched successfully",
                "data", category
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateCategory(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Category category = new Category();
            category.setCategoryName((String) body.get("categoryName"));
            category.setDescription((String) body.get("description"));
            category.setActive((Boolean) body.get("active"));
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Category updated successfully",
                "data", updatedCategory
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteCategory(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            categoryService.deleteCategory(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Category deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
