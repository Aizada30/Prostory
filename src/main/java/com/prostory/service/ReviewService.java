package com.prostory.service;

import com.prostory.dto.request.ReviewRequest;
import com.prostory.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse addReview(String email, ReviewRequest request);
    List<ReviewResponse> getReviewsByProduct(Long productId);
    void deleteReview(String email, Long reviewId);
}
