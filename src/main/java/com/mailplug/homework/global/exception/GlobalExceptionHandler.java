package com.mailplug.homework.global.exception;

import com.mailplug.homework.global.response.CommonResponse;
import com.mailplug.homework.global.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> handleBusinessException(final BusinessException exception) {

        logger.warn("비즈니스 로직에서 {}예외가 발생하였습니다.", exception.getErrorType());

        return buildResponse(exception.getErrorType());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> handleRuntimeException() {

        logger.error("서버 내부적 오류가 발생하였습니다.");

        return buildResponse(ErrorType.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {

        final Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));

        final String strErrors = String.valueOf(errors);

        logger.warn("필수 파라미터가 유효성 검증에 실패하였습니다.");

        return buildResponse(ErrorType.dtoInvalid(strErrors));
    }

    private ResponseEntity<CommonResponse> buildResponse(final ErrorType errorType) {

        final ErrorResponse errorResponse = ErrorResponse.newInstance(errorType);
        final CommonResponse response = CommonResponse.newInstance(errorResponse);

        return ResponseEntity.status(errorType.getHttpStatus()).body(response);
    }
}
