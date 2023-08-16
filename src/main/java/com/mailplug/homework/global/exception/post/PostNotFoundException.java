package com.mailplug.homework.global.exception.post;

import com.mailplug.homework.global.exception.BusinessException;
import com.mailplug.homework.global.exception.ErrorType;

public class PostNotFoundException extends BusinessException {

    public PostNotFoundException() {
        super(ErrorType.POST_NOT_FOUND);
    }

    public ErrorType getErrorType() {
        return ErrorType.POST_NOT_FOUND;
    }
}
