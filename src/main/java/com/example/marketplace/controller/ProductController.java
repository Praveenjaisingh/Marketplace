package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Product;
import com.example.marketplace.service.ProductService;

@Component
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public ServerResponse createProduct(ServerRequest request) throws Exception {
        try {
            Product product = request.body(Product.class);
            Product savedProduct = productService.createProduct(product);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Product created successfully",
                "data", savedProduct
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllProducts(ServerRequest request) throws Exception {
        try {
            List<Product> allProducts = productService.getAllProducts();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Products fetched successfully",
                "data", allProducts
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getProductById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Product product = productService.getProductById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Product fetched successfully",
                "data", product
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateProduct(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Product product = new Product();
            product.setSellerId(((Number) body.get("sellerId")).intValue());
            product.setCategoryId(((Number) body.get("categoryId")).intValue());
            product.setProductName((String) body.get("productName"));
            product.setDescription((String) body.get("description"));
            product.setPrice(((Number) body.get("price")).doubleValue());
            product.setBrand((String) body.get("brand"));
            product.setImageUrl((String) body.get("imageUrl"));
            product.setActive((Boolean) body.get("active"));
            Product updatedProduct = productService.updateProduct(id, product);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Product updated successfully",
                "data", updatedProduct
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteProduct(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            productService.deleteProduct(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Product deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
