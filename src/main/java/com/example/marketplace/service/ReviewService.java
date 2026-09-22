package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Review;
import com.example.marketplace.repository.ReviewRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository, ValidationUtil validationUtil) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
    }


    private void validateForeignKeys(Review review) {
        if (!userRepository.existsById(review.getUserId())) {
            validationUtil.fail("userId", "references a record that does not exist");
        }
        if (!productRepository.existsById(review.getProductId())) {
            validationUtil.fail("productId", "references a record that does not exist");
        }
    }
    public Review createReview(Review review) {
        validationUtil.validate(review);
        validateForeignKeys(review);
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(int id) {
        return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public Review updateReview(int id, Review reviewDetails) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setUserId(reviewDetails.getUserId());
        review.setProductId(reviewDetails.getProductId());
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        validationUtil.validate(review);
        validateForeignKeys(review);
        return reviewRepository.save(review);
    }

    public void deleteReview(int id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
    }
}
