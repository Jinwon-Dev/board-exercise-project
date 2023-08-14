package com.mailplug.homework.global.security;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationExtractor {

    public String extract(final HttpServletRequest request, final String type) {

        final var headers = request.getHeaders("Authorization");

        while (headers.hasMoreElements()) {

            final String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }

        return Strings.EMPTY;
    }
}
