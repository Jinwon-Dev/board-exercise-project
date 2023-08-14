package com.mailplug.homework.domain.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import static com.mailplug.homework.global.exception.board.BoardExceptionExecutor.BoardTypeInvalid;

@RequiredArgsConstructor
@Getter
@JsonDeserialize(using = BoardTypeDeserializer.class)
public enum BoardType {

    NOTICE("Notice"), INQUIRY("Inquiry"), FREE("Free");

    private final String value;

    @JsonCreator
    public static BoardType fromString(final String value) {

        if (!StringUtils.hasText(value)) throw BoardTypeInvalid();

        try {
            return BoardType.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw BoardTypeInvalid();
        }
    }
}
