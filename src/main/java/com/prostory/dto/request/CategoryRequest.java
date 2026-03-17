package com.prostory.dto.request;

import jakarta.validation.constraints.NotBlank;


public record CategoryRequest(
        @NotBlank
        String name
) {
}
