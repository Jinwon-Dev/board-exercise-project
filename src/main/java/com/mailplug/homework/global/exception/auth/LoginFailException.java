package com.mailplug.homework.global.exception.auth;

import com.mailplug.homework.global.exception.BusinessException;
import com.mailplug.homework.global.exception.ErrorType;

public class LoginFailException extends BusinessException {

    public LoginFailException() {
        super(ErrorType.LOGIN_FAIL);
    }

    public ErrorType getErrorType() {
        return ErrorType.LOGIN_FAIL;
    }
}
