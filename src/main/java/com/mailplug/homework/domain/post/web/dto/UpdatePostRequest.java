package com.mailplug.homework.domain.post.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePostRequest(
        @NotBlank @Size(max = 100) String title,
        @NotBlank String content
) {
}
