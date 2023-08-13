package com.mailplug.homework.global.response;

import com.mailplug.homework.global.exception.ErrorType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String errorCode;
    private String errorMessage;

    public static ErrorResponse newInstance(final ErrorType errorType) {
        ErrorResponse response = new ErrorResponse();
        response.errorCode = errorType.getCode();
        response.errorMessage = errorType.getMessage();
        return response;
    }
}
