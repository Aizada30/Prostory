package com.prostory.dto;

import com.prostory.enums.Role;
import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String email,
        Role role,
        String token
) {
}
