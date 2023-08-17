package com.mailplug.homework.global.exception.post;

public class PostExceptionExecutor {

    public static PostNotFoundException PostNotFound() {
        return new PostNotFoundException();
    }

    public static WriterNotSameException WriterForbidden() {
        return new WriterNotSameException();
    }
}
