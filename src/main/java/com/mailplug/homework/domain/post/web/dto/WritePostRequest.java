package com.mailplug.homework.domain.post.web.dto;

import com.mailplug.homework.domain.board.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WritePostRequest(
        @NotBlank @Size(max = 100, message = "입력 길이를 초과하였습니다.") String title,
        @NotBlank String content,
        @NotNull BoardType boardType
) {
}
