package com.eminyidle.auth.auth.controller;

import com.eminyidle.auth.auth.cookie.CookieUtil;
import com.eminyidle.auth.auth.dto.req.TokenReq;
import com.eminyidle.auth.auth.dto.res.TokenRes;
import com.eminyidle.auth.auth.jwt.JWTUtil;
import com.eminyidle.auth.auth.service.AuthService;
import com.eminyidle.auth.oauth2.dto.CustomOAuth2User;
import com.eminyidle.auth.redis.RedisPrefix;
import com.eminyidle.auth.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RedisService redisService;
    private final JWTUtil jwtUtil;

    @GetMapping("/login")
    public void redirectToGoogleOAuth2(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        // 사용자 정의 경로로 OAuth2 로그인 페이지로 리디렉션
        String redirectUrl = request.getContextPath() + "/oauth2/authorization/google";
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal CustomOAuth2User user) {
        // 레디스 정보 제거
        authService.logoutUser(user.getUserId());
        // 쿠키 지우기
        CookieUtil.deleteTokenCookie(request, response);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenRes> tokenRegenerate(@RequestBody TokenReq tokenReq) {
        String inputRefreshToken = tokenReq.getRefreshToken();

        if (jwtUtil.isValid(inputRefreshToken)) {
            String userId = jwtUtil.getUserId(inputRefreshToken);
            String refreshTokenKey = RedisPrefix.REFRESH_TOKEN.prefix() + userId;
            String refreshToken = (String) redisService.getValues(refreshTokenKey);

            // refreshToken 같으면 token 재발급
            if (refreshToken != null && refreshToken.equals(inputRefreshToken)) {
                String accessToken = jwtUtil.createJwt(userId, 1000 * 60 * 60L);
                String newRefreshToken = jwtUtil.regenerateRefreshJwt(userId, refreshToken,
                    1000 * 60 * 60 * 24 * 14L, 1000 * 60 * 60 * 24 * 7L);
                redisService.setValues(refreshTokenKey, newRefreshToken);

                return ResponseEntity.ok().body(TokenRes
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .build()
                );
            }
        }

        // 리프레시 토큰 만료 또는 불일치 하는 경우
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<CustomOAuth2User> test(@AuthenticationPrincipal CustomOAuth2User user) {
        return ResponseEntity.ok().body(user);
    }
}