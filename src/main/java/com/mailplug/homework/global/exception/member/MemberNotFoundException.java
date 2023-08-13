package com.mailplug.homework.global.exception.member;

import com.mailplug.homework.global.exception.BusinessException;
import com.mailplug.homework.global.exception.ErrorType;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException() {
        super(ErrorType.MEMBER_NOT_FOUND);
    }

    public ErrorType getErrorType() {
        return ErrorType.MEMBER_NOT_FOUND;
    }
}
