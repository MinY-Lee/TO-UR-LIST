package com.eminyidle.auth.auth.jwt;

import com.eminyidle.auth.auth.cookie.CookieUtil;
import com.eminyidle.auth.auth.dto.TokenCookie;
import com.eminyidle.auth.auth.exception.TokenInvalidException;
import com.eminyidle.auth.redis.RedisService;
import com.eminyidle.auth.user.dto.User;
import com.eminyidle.auth.user.exception.UserNotExistException;
import com.eminyidle.auth.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 기존에 로그인 했던 사용자 판별 및 토큰 유효성 검사 실시
 * OAuth 로그인 전 확인 필터
 */
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.debug("----------------JWT 필터 타기 -----------------");
        log.debug("[JWTFilter] - 필터 시작");
        try {
            validateRefreshToken(request, response);
            filterChain.doFilter(request, response);
        }
        catch (TokenInvalidException | UserNotExistException e) {
            deleteCookie(request, response);
            log.error("[JWTFilter] - {}", e.getMessage());
            filterChain.doFilter(request, response);
        }

    }

    private void validateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws UserNotExistException{
        TokenCookie tokenCookie = CookieUtil.resolveToken(request);
        // refreshToken 검증
        Cookie refreshTokenCookie = tokenCookie.getRefreshTokenCookie();
        // refreshToken 없거나 이상이 있는 경우
        if (refreshTokenCookie == null || !jwtUtil.isValid(refreshTokenCookie.getValue())) {
            throw new TokenInvalidException("refreshToken 오류");
        }

        String userId = jwtUtil.getUserId(refreshTokenCookie.getValue());
        log.debug("[JWTFilter] - 토큰 userId:{}", userId);

        // accessToken 검증 및 재발급
        validateAccessToken(userId, tokenCookie, response);

        // 로그인 유지
        authenticateUser(userId);
    }
    // accessToken 검증 후 오류시 재발급
    private void validateAccessToken(String userId, TokenCookie tokenCookie, HttpServletResponse response) {
        Cookie accessTokenCookie = tokenCookie.getAccessTokenCookie();

        if (accessTokenCookie == null || !jwtUtil.isValid(accessTokenCookie.getValue())) {
            // 유효하지 않은 경우 기존 쿠키 삭제 (accessTokenCookie가 null이 아닐 때만 해당)
            if (accessTokenCookie != null) {
                CookieUtil.deleteCookie(accessTokenCookie, response);
            }
            // 새로운 accessToken 생성
            String newAccessToken = jwtUtil.createJwt(userId, 1000 * 60 * 60L * 3); // 3시간 유효기간
            Cookie newCookie = new Cookie("accessToken", newAccessToken);
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 3);
            response.addCookie(newCookie);
        }
    }

    private void authenticateUser(String userId) throws UserNotExistException{
        //user를 생성하여 값 set
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId).orElseThrow(UserNotExistException::new);
        //스프링 시큐리티 인증 토큰 생성 - 우리가 사용할 내용은 유저 객체
        Authentication authToken = new UsernamePasswordAuthenticationToken(user, null, null);

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response){
        TokenCookie tokenCookie = CookieUtil.resolveToken(request);
        Cookie accessTokenCookie = tokenCookie.getAccessTokenCookie();
        Cookie refreshTokenCookie = tokenCookie.getRefreshTokenCookie();

        if(refreshTokenCookie != null) {
            redisService.deleteValues(refreshTokenCookie.getValue());
            CookieUtil.deleteCookie(refreshTokenCookie, response);
        }

        if(accessTokenCookie != null) {
            CookieUtil.deleteCookie(accessTokenCookie, response);
        }
    }
}