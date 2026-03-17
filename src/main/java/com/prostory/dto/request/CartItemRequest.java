package com.prostory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CartItemRequest(
        @Schema(example = "1")
        Long productId,

        @Schema(example = "1")
        int quantity
) {
}
