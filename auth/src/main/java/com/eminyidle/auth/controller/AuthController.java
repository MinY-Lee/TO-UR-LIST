package com.eminyidle.auth.controller;

import com.eminyidle.auth.cookie.CookieUtil;
import com.eminyidle.auth.dto.res.TokenRes;
import com.eminyidle.auth.jwt.JWTUtil;
import com.eminyidle.auth.service.AuthService;
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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
	public void logout(HttpServletRequest request, HttpServletResponse response,
		@CookieValue("accessToken") String accessToken) {

		if (accessToken != null) {
			// 레디스 정보 제거
			authService.logoutUser(accessToken);
		}
		// 쿠키 지우기
		CookieUtil.deleteTokenCookie(request, response);
	}

	@GetMapping("/token")
	public ResponseEntity<TokenRes> tokenRegenerate(@RequestHeader String userId,
		@RequestHeader String refreshToken) {

		if (userId == null) {
			return ResponseEntity.badRequest().build();
		}

		if (refreshToken == null) {
			return ResponseEntity.badRequest().build();
		}

		String refreshTokenKey = RedisPrefix.REFRESH_TOKEN.prefix() + userId;
		String savedRefreshToken = (String) redisService.getValues(refreshTokenKey);

		// refreshToken 같으면 token 재발급
		if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
			return ResponseEntity.badRequest().build();
		}

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

	@DeleteMapping("/user")
	public ResponseEntity<Void> deleteUser(@RequestHeader String userId) {
		authService.deleteUser(userId);
		return ResponseEntity.ok().build();
	}
}