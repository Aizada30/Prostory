package com.prostory.dto.response;

import java.time.LocalDate;

public record ReviewResponse(
        Long id,
        String text,
        int rating,
        LocalDate createdAt,
        String userName,
        String productName
) {
}
