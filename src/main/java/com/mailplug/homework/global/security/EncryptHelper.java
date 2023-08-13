package com.mailplug.homework.global.security;

public interface EncryptHelper {

    String encrypt(final String password, final String salt);

    boolean isMatch(final String password, final String hashed);
}
