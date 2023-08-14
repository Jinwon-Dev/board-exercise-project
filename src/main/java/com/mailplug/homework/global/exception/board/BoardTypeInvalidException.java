package com.mailplug.homework.global.exception.board;

import com.mailplug.homework.global.exception.BusinessException;
import com.mailplug.homework.global.exception.ErrorType;

public class BoardTypeInvalidException extends BusinessException {

    public BoardTypeInvalidException() {
        super(ErrorType.BOARD_TYPE_INVALID);
    }

    public ErrorType getErrorType() {
        return ErrorType.BOARD_TYPE_INVALID;
    }
}
