package com.prostory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewRequest(
        @NotBlank(message = "Текст обязателен")
        String text,

        @Min(1) @Max(5)
        int rating,

        @Schema(example = "1")
        Long productId
) {
}
