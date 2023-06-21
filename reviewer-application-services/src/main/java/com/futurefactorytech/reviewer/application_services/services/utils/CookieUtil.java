package com.futurefactorytech.reviewer.application_services.services.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.Optional;

public final class CookieUtil {

    @Value("${application-config.api-domain}")
    private static String serverDomain;

    public static String extractCookieValue(Cookie[] cookies, String cookieName) {
        if (cookies != null) {
            Optional<Cookie> cookie = Arrays.stream(cookies)
                    .filter(x -> x.getName().equals(cookieName))
                    .findFirst();
            return cookie.map(Cookie::getValue).orElse(null);
        }
        return null;
    }

    public static Cookie extractCookie(Cookie[] cookies, String cookieName) {
        if (cookies != null) {
            Optional<Cookie> cookie = Arrays.stream(cookies)
                    .filter(x -> x.getName().equals(cookieName))
                    .findFirst();
            return cookie.orElse(null);
        }
        return null;
    }

    public static Cookie createCookie(String cookieName, String value, int maxAge, boolean isSecure,
                                      boolean isHttpOnly) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setSecure(isSecure);
        cookie.setHttpOnly(isHttpOnly);
        cookie.setPath("/");
        cookie.setDomain(serverDomain);
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}
