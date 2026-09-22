package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Review;
import com.example.marketplace.service.ReviewService;

@Component
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public ServerResponse createReview(ServerRequest request) throws Exception {
        try {
            Review review = request.body(Review.class);
            Review savedReview = reviewService.createReview(review);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Review created successfully",
                "data", savedReview
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllReviews(ServerRequest request) throws Exception {
        try {
            List<Review> allReviews = reviewService.getAllReviews();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Reviews fetched successfully",
                "data", allReviews
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getReviewById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Review review = reviewService.getReviewById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Review fetched successfully",
                "data", review
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateReview(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Review review = new Review();
            review.setUserId(((Number) body.get("userId")).intValue());
            review.setProductId(((Number) body.get("productId")).intValue());
            review.setRating(((Number) body.get("rating")).intValue());
            review.setComment((String) body.get("comment"));
            Review updatedReview = reviewService.updateReview(id, review);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Review updated successfully",
                "data", updatedReview
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteReview(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            reviewService.deleteReview(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Review deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
