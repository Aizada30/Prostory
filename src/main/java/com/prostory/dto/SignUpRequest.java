package com.prostory.dto;

import lombok.Builder;

@Builder
public record SignUpRequest(
        String userName,
        String phoneNumber,
        String email,
        String password
) {
}
