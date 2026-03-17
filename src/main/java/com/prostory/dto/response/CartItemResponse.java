package com.prostory.dto.response;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        String productName,
        String productImage,
        BigDecimal productPrice,
        int quantity,
        BigDecimal totalPrice
) {
}
