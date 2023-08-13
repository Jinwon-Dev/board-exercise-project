package com.mailplug.homework.global.exception.member;

import com.mailplug.homework.global.exception.BusinessException;
import com.mailplug.homework.global.exception.ErrorType;

public class EmailOverlapException extends BusinessException {

    public EmailOverlapException() {
        super(ErrorType.MEMBER_EMAIL_OVERLAP);
    }

    public ErrorType getErrorType() {
        return ErrorType.MEMBER_EMAIL_OVERLAP;
    }
}
