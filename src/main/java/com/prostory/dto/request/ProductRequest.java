package com.prostory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ProductRequest(
        String name,
        String description,
        BigDecimal price,
        @Schema(example = "10")
        int quantity,
        String image,
        @Schema(example = "1")
        Long categoryId
) {
}