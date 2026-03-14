package com.prostory.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "password should not be empty")
        String newPassword
) {
}
