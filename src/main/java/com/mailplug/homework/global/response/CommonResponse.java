package com.mailplug.homework.global.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommonResponse<T> {

    private T data;
    private LocalDateTime timestamp;

    public static <T> CommonResponse<T> newInstance(final T data) {
        final CommonResponse response = new CommonResponse();
        response.data = data;
        response.timestamp = LocalDateTime.now().withNano(0);
        return response;
    }
}
