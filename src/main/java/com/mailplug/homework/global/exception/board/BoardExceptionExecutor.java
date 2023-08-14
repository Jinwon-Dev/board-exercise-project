package com.mailplug.homework.global.exception.board;

public class BoardExceptionExecutor {

    public static BoardTypeInvalidException BoardTypeInvalid() {
        return new BoardTypeInvalidException();
    }
}
