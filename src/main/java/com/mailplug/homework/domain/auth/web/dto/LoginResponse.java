package com.mailplug.homework.domain.auth.web.dto;

public record LoginResponse(
        String email,
        String accessToken,
        String tokenType
) {
}
