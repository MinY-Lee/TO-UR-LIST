package com.eminyidle.auth.cookie;

import com.eminyidle.auth.dto.TokenCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 쿠키 정보 가져오기
 */
public class CookieUtil {

    private CookieUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static TokenCookie resolveToken(HttpServletRequest request) {
        Cookie accessTokenCookie = null;
        Cookie refreshTokenCookie = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessTokenCookie = cookie;
                } else if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenCookie = cookie;
                }
            }
        }
        return TokenCookie.builder()
            .accessTokenCookie(accessTokenCookie)
            .refreshTokenCookie(refreshTokenCookie)
            .build();
    }

    public static void deleteTokenCookie(HttpServletRequest request,
        HttpServletResponse response) {
        TokenCookie tokenCookie = CookieUtil.resolveToken(request);

        Cookie accessTokenCookie = tokenCookie.getAccessTokenCookie();
        Cookie refreshTokenCookie = tokenCookie.getRefreshTokenCookie();

        if (accessTokenCookie != null) {
            deleteCookie(accessTokenCookie, response);
        }
        if (refreshTokenCookie != null) {
            deleteCookie(refreshTokenCookie, response);
        }
    }

    public static void deleteCookie(Cookie currentCookie, HttpServletResponse response) {
        // 해당 쿠키 제거
        currentCookie.setPath("/");
        currentCookie.setMaxAge(0);
        response.addCookie(currentCookie);
    }
}
