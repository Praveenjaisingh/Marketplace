package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Product;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.SellerRepository;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ValidationUtil validationUtil;

    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository,
            CategoryRepository categoryRepository, ValidationUtil validationUtil) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.categoryRepository = categoryRepository;
        this.validationUtil = validationUtil;
    }

    private void validateForeignKeys(Product product) {
        if (!sellerRepository.existsById(product.getSellerId())) {
            validationUtil.fail("sellerId", "references a seller that does not exist");
        }
        if (!categoryRepository.existsById(product.getCategoryId())) {
            validationUtil.fail("categoryId", "references a category that does not exist");
        }
    }

    public Product createProduct(Product product) {
        validationUtil.validate(product);
        validateForeignKeys(product);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(int id, Product productDetails) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setSellerId(productDetails.getSellerId());
        product.setCategoryId(productDetails.getCategoryId());
        product.setProductName(productDetails.getProductName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setBrand(productDetails.getBrand());
        product.setImageUrl(productDetails.getImageUrl());
        product.setActive(productDetails.isActive());
        validationUtil.validate(product);
        validateForeignKeys(product);
        return productRepository.save(product);
    }

    public void deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
}
