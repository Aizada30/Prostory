package com.prostory.dto.request;

import com.prostory.validation.EmailValid;
import com.prostory.validation.NameValid;
import com.prostory.validation.PasswordValid;
import com.prostory.validation.PhoneNumberValid;
import lombok.Builder;

@Builder
public record SignUpRequest(
        @NameValid
        String userName,
        @PhoneNumberValid
        String phoneNumber,
        @EmailValid
        String email,
        @PasswordValid
        String password
) {
}
