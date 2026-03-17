package com.prostory.dto.request;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ProductRequest(
        String name,
        String description,
        BigDecimal price,
        int quantity,
        String image,
        Long categoryId
) {
}