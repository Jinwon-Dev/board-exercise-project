package com.mailplug.homework.global.exception.auth;

public class AuthExceptionExecutor {

    public static LoginFailException LoginFail() {
        return new LoginFailException();
    }

    public static TokenNotExistException UnAuthorized() {
        return new TokenNotExistException();
    }
}
