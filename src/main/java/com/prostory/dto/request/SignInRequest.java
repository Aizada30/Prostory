package com.prostory.dto.request;

import com.prostory.validation.EmailValid;
import com.prostory.validation.PasswordValid;
import lombok.Builder;

@Builder
public record SignInRequest(
        @EmailValid
        String email,
        @PasswordValid
        String password
) {
}
