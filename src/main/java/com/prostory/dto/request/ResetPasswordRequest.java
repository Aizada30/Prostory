package com.prostory.dto.request;

import com.prostory.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        @NotBlank(message = "password should not be empty")
        @PasswordValid
        String newPassword
) {
}
