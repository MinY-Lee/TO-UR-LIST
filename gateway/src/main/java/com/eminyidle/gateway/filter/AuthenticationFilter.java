package com.eminyidle.gateway.filter;


import com.eminyidle.gateway.dto.res.TokenRes;
import com.eminyidle.gateway.jwt.JWTUtil;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

	private final JWTUtil jwtUtil;

	@Value("${INTERNAL_KEY}")
	private String INTERNAL_KEY;

	@Value("${AUTH_SERVER_URL}")
	private String AUTH_SERVER_URL;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest serverHttpRequest = exchange.getRequest();

		// token이 둘 다 없을 경우
		if (this.isAccessTokenMissing(serverHttpRequest) && this.isRefreshTokenMissing(serverHttpRequest)) {
			log.info("access refresh 없음");
			return this.onError(exchange, HttpStatus.UNAUTHORIZED);
		}

		MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();


		HttpCookie accessTokenCookie = this.isAccessTokenMissing(serverHttpRequest) ? new HttpCookie("accessToken", ""): cookies.getFirst("accessToken");
		HttpCookie refreshTokenCookie = this.isRefreshTokenMissing(serverHttpRequest) ? new HttpCookie("refreshToken", ""): cookies.getFirst("refreshToken");

		// access token 유효
		if (jwtUtil.isValid(accessTokenCookie.getValue())) {
			log.info("access 토큰 유효");
			return continueWithUserIdHeader(exchange, chain, accessTokenCookie.getValue());
		}

		// access token 만료, refresh token 만료
		if (jwtUtil.isInvalid(refreshTokenCookie.getValue())) {
			log.info("access, refresh 만료");
			return this.onError(exchange, HttpStatus.UNAUTHORIZED);
		}

		// access token 만료, refresh token 유효
		String userId = jwtUtil.getUserId(refreshTokenCookie.getValue());
		log.info("refresh 유효");
		log.info("auth server url: {}", AUTH_SERVER_URL);
		WebClient webClient = WebClient.builder()
			.baseUrl(AUTH_SERVER_URL)
			.defaultHeader("InternalKey", INTERNAL_KEY)
			.build();

		return webClient.get()
			.uri("/auth/token")
			.header("UserId", userId)
			.header("RefreshToken", refreshTokenCookie.getValue())
			.retrieve()
			.bodyToMono(TokenRes.class)
			.flatMap(tokenRes -> {
				updateResponse(exchange, tokenRes.getAccessToken(), tokenRes.getRefreshToken());
				log.info(tokenRes.getAccessToken());
				return continueWithUserIdHeader(exchange, chain, tokenRes.getAccessToken());
			})
			.onErrorResume(error -> {
				log.error("{}", error);
				log.info("재발급 에러");
				return this.onError(exchange, HttpStatus.UNAUTHORIZED);
			});
	}

	private Mono<Void> continueWithUserIdHeader(ServerWebExchange exchange, GatewayFilterChain chain, String accessToken) {
		String userId = jwtUtil.getUserId(accessToken);
		ServerHttpRequest updatedRequest = exchange.getRequest().mutate().header("UserId", userId).build();
		ServerWebExchange updatedExchange = exchange.mutate().request(updatedRequest).build();

		return chain.filter(updatedExchange);
	}

	private void updateResponse(ServerWebExchange exchange, String accessToken,
		String refreshToken) {
		ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
			.maxAge(60 * 60 * 30).path("/").build();
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
			.maxAge(1000 * 60 * 60 * 24 * 14L).path("/").build();

		exchange.getResponse().addCookie(accessTokenCookie);
		exchange.getResponse().addCookie(refreshTokenCookie);
	}

	private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {

		ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken")
			.maxAge(0).path("/").build();
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken")
			.maxAge(0).path("/").build();
		ServerHttpResponse serverHttpResponse = exchange.getResponse();
		serverHttpResponse.setStatusCode(httpStatus);
		serverHttpResponse.addCookie(accessTokenCookie);
		serverHttpResponse.addCookie(refreshTokenCookie);
		return serverHttpResponse.setComplete();
	}

	private boolean isAccessTokenMissing(ServerHttpRequest serverHttpRequest) {
		return !serverHttpRequest.getCookies().containsKey("accessToken");
	}

	private boolean isRefreshTokenMissing(ServerHttpRequest serverHttpRequest) {
		return !serverHttpRequest.getCookies().containsKey("refreshToken");
	}

}
