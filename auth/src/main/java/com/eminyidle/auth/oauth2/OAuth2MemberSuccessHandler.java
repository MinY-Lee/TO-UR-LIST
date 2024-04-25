package com.eminyidle.auth.oauth2;

import com.eminyidle.auth.auth.jwt.JWTUtil;
import com.eminyidle.auth.oauth2.dto.CustomOAuth2User;
import com.eminyidle.auth.redis.RedisPrefix;
import com.eminyidle.auth.redis.RedisService;
import com.eminyidle.auth.user.dto.User;
import com.eminyidle.auth.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
@Slf4j
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final String mainPage;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException, IOException {

        // 사용자 정보 가져오기 - 로그인이 완료된 인증 객체(OAuth2AuthenticationToken)에서 유저 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 사용자 이메일 가져오기
        String userEmail = oAuth2User.getUserEmail();
        User user = userRepository.findByUserEmailAndDeletedAtIsNull(userEmail).get();
        String userId = user.getUserId();
        String key = RedisPrefix.REFRESH_TOKEN.prefix() + user.getUserId();

        //accessToken 생성
        String accessToken = jwtUtil.createJwt(user.getUserId(), 1000 * 60 * 60L * 3);

        //refreshToken 생성
        String refreshToken =
                redisService.hasKey(key) ? jwtUtil.regenerateRefreshJwt(userId,
                        (String) redisService.getValues(key),
                        1000 * 60 * 60 * 24 * 14L,
                        1000 * 60 * 60 * 24 * 7L)
                        : jwtUtil.createJwt(user.getUserId(), 1000 * 60 * 60 * 24 * 14L);

        //만료시간
        Long expiredMillis = jwtUtil.getExpiredDate(refreshToken);
        Duration expiredTime = Duration.ofMillis(expiredMillis);

        //Redis에 refreshToken 저장 - key는 "loginRefresh:jwt값"
        redisService.setValues(key, refreshToken, expiredTime);
        log.debug("RefreshToken: {}", redisService.getValues(key));

//        //response에 토큰 담아서 반환
//        ObjectMapper objectMapper = new ObjectMapper();
//        // content -type
//        response.setContentType("application/json");
//        response.setCharacterEncoding("utf-8");
//
//        LoginRes loginRes = new LoginRes();
//        loginRes.setAccessToken(accessToken);
//        loginRes.setRefreshToken(refreshToken);
//
//        // JSON 형태로 변환하기
//        // {"accessToken" : "12344", "refreshToken" : "dasgfdsa"}
//        String result = objectMapper.writeValueAsString(loginRes);
//
//        response.getWriter().write(result);
        int expiredSeconds = (int) (expiredMillis / 1000);
        // 쿠키에 데이터 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
//        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(60 * 60 * 3);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(expiredSeconds);
        response.addCookie(refreshTokenCookie);
        // 리다이렉트 사용
        response.sendRedirect(mainPage);
    }
}

