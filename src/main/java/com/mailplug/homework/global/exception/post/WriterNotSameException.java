package com.mailplug.homework.global.exception.post;

import com.mailplug.homework.global.exception.BusinessException;
import com.mailplug.homework.global.exception.ErrorType;

public class WriterNotSameException extends BusinessException {

    public WriterNotSameException() {
        super(ErrorType.WRITER_FORBIDDEN);
    }

    public ErrorType getErrorType() {
        return ErrorType.WRITER_FORBIDDEN;
    }
}
