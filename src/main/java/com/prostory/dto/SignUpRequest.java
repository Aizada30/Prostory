package com.prostory.dto;

import com.prostory.validation.EmailValid;
import com.prostory.validation.NameValid;
import com.prostory.validation.PasswordValid;
import com.prostory.validation.PhoneNumberValid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SignUpRequest(
        @NameValid
        @NotBlank
        String userName,
        @PhoneNumberValid
        @NotBlank
        String phoneNumber,
        @EmailValid
        @NotBlank
        String email,
        @PasswordValid
        @NotBlank
        String password
) {
}
