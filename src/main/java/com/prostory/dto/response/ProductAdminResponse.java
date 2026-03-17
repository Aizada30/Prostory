package com.prostory.dto.response;

import java.math.BigDecimal;

public record ProductAdminResponse(
        Long id,
        String article,
        String name,
        String description,
        BigDecimal price,
        int discountPercent,
        double rating,
        String image,
        boolean inStock,
        String categoryName,
        int quantity
) {
}
