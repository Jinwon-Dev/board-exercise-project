package com.mailplug.homework.domain.post.web.dto;

public record UpdatePostResponse(
        Long postId,
        String title,
        String content
) {
}
