package com.mailplug.homework.domain.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mailplug.homework.global.exception.board.BoardExceptionExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Enum으로 게시판을 정의한 이유
 *
 * : 확장이 불가능하다는 단점이 있지만 게시판의 경우 회원이 직접 생성할 수 없고,
 * : 미리 정의해둔 값을 바탕으로 게시글을 생성하기에 Enum으로 정의해두었습니다.
 */
@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = BoardTypeDeserializer.class)
public enum BoardType {

    NOTICE("Notice"), INQUIRY("Inquiry"), FREE("Free");

    private final String value;

    @JsonCreator
    public static BoardType fromString(final String value) {

        if (!StringUtils.hasText(value)) throw BoardExceptionExecutor.BoardTypeInvalid();

        try {
            return BoardType.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw BoardExceptionExecutor.BoardTypeInvalid();
        }
    }
}
