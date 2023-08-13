package com.mailplug.homework.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BusinessException extends RuntimeException {

    private final ErrorType errorType;
}
