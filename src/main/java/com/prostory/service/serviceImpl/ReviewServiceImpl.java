package com.prostory.service.serviceImpl;

import com.prostory.dto.request.ReviewRequest;
import com.prostory.dto.response.ReviewResponse;
import com.prostory.entity.Product;
import com.prostory.entity.Review;
import com.prostory.entity.User;
import com.prostory.exception.exceptions.NotFoundException;
import com.prostory.repository.ProductRepository;
import com.prostory.repository.ReviewRepository;
import com.prostory.repository.UserRepository;
import com.prostory.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse addReview(String email, ReviewRequest request) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Review review = Review.builder()
                .text(request.text())
                .rating(request.rating())
                .createdAt(LocalDate.now())
                .product(product)
                .user(user)
                .build();

        return mapToResponse(reviewRepository.save(review));
    }

    @Override
    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return reviewRepository.findAllByProduct_Id(productId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteReview(String email, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getUser().getUserInfo().getEmail().equals(email)) {
            throw new RuntimeException("Нет прав для удаления этого отзыва");
        }

        reviewRepository.delete(review);
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getText(),
                review.getRating(),
                review.getCreatedAt(),
                review.getUser().getUserInfo().getEmail(),
                review.getProduct().getName()
        );
    }
}
