package com.mailplug.homework.global.exception.auth;

import com.mailplug.homework.global.exception.BusinessException;
import com.mailplug.homework.global.exception.ErrorType;

public class TokenNotExistException extends BusinessException {

    public TokenNotExistException() {
        super(ErrorType.UNAUTHORIZED);
    }

    public ErrorType getErrorType() {
        return ErrorType.UNAUTHORIZED;
    }
}
