package com.mailplug.homework.domain.post.web.dto;

public record WritePostResponse(
        Long id,
        String title,
        String content,
        Long memberId
) {
}
