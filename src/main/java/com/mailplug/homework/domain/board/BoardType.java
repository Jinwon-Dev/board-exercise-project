package com.mailplug.homework.domain.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mailplug.homework.global.exception.board.BoardExceptionExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

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
