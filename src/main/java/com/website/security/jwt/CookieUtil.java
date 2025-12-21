package com.website.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }

    public static void addRefreshCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);      // HTTPS면 true
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        // SameSite는 Servlet Cookie API에서 직접 설정 어려워서 헤더로 세팅하는 방식도 씀
        response.addCookie(cookie);
    }
}
