package com.prostory.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        Long id,
        String article,
        String name,
        String description,
        BigDecimal price,
        int discountPercent,
        double rating,
        String image,
        boolean inStock,
        String categoryName
        ) {
}
