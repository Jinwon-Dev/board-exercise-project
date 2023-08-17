package com.mailplug.homework.domain.post.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequest(
        @NotBlank String title,
        @NotBlank String content
) {
}
