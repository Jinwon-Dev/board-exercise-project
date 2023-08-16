package com.mailplug.homework.domain.post.web.dto;

import java.time.LocalDateTime;

public record ReadPostResponse(
        Long id,
        String title,
        String content,
        String writer,
        LocalDateTime createAt,
        int views
) {
}
