package com.mailplug.homework.domain.post.web.dto;

import java.time.LocalDateTime;

public record ReadPostListResponse(
        Long id,
        String title,
        String writer,
        LocalDateTime createAt,
        int views
) {
}
