package com.mailplug.homework.global.exception;

import com.mailplug.homework.global.response.CommonResponse;
import com.mailplug.homework.global.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> handleBusinessException(final BusinessException exception) {

        return buildResponse(exception.getErrorType());
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<CommonResponse> handleRuntimeException(final RuntimeException exception) {
//
//        return buildResponse(ErrorType.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {

        final List<String> globalErrors = exception.getGlobalErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();

        final List<String> fieldErrors = exception.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        final List<String> allErrors = new ArrayList<>();
        allErrors.addAll(globalErrors);
        allErrors.addAll(fieldErrors);

        final String message = String.join(", ", allErrors);

        return buildResponse(ErrorType.dtoInvalid(message));
    }

    private ResponseEntity<CommonResponse> buildResponse(final ErrorType errorType) {

        final ErrorResponse errorResponse = ErrorResponse.newInstance(errorType);
        final CommonResponse response = CommonResponse.newInstance(errorResponse);

        return ResponseEntity.status(errorType.getHttpStatus()).body(response);
    }
}
