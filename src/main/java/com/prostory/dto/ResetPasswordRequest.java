package com.prostory.dto;

import com.prostory.validation.PasswordValid;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "password should not be empty")
        @PasswordValid
        String newPassword
) {
}
