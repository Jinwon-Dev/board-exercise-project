package com.mailplug.homework.global.exception.member;

public class MemberExceptionExecutor {

    public static MemberNotFoundException MemberNotFound() {
        return new MemberNotFoundException();
    }

    public static EmailOverlapException EmailOverlap() {
        return new EmailOverlapException();
    }
}
