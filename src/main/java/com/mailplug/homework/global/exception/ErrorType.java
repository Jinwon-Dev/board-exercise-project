package com.mailplug.homework.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    // USER
    MEMBER_NOT_FOUND("USER-001", "회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_EMAIL_OVERLAP("USER-002", "중복된 이메일입니다.", HttpStatus.CONFLICT),

    // AUTH
    UNAUTHORIZED("AUTH-001", "토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_FAIL("AUTH-002", "로그인에 실패하였습니다.", HttpStatus.BAD_REQUEST),

    // BOARD
    BOARD_TYPE_INVALID("BOARD-001", "게시판 종류가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // POST
    POST_NOT_FOUND("POST-001", "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    WRITER_FORBIDDEN("POST-002", "해당 게시글을 작성한 작성자가 아닙니다.", HttpStatus.FORBIDDEN),

    // COMMON
    DTO_PARAMETER_INVALID("DTO-001", "DTO의 파라미터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("SERVER-001", "서버 내부적 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private String message;
    private final HttpStatus httpStatus;

    ErrorType(final String code, final String message, final HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private void setMessage(final String message) {
        this.message = message;
    }

    static ErrorType dtoInvalid(final String message) {
        final ErrorType errorType = DTO_PARAMETER_INVALID;
        errorType.setMessage(message);
        return errorType;
    }
}
