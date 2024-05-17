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

	@Value("${AUTH_SERVER_URL")
	private String AUTH_SERVER_URL;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest serverHttpRequest = exchange.getRequest();

		// token이 둘 다 없을 경우
		if (this.isAccessTokenMissing(serverHttpRequest) && this.isRefreshTokenMissing(serverHttpRequest)) {
			return this.onError(exchange, HttpStatus.UNAUTHORIZED);
		}

		MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();


		HttpCookie accessTokenCookie = new HttpCookie("accessToken", "");
		HttpCookie refreshTokenCookie = new HttpCookie("refreshToken", "");

		if(!this.isAccessTokenMissing(serverHttpRequest)) {
			accessTokenCookie = cookies.get("accessToken").get(0);
		}

		if(!this.isRefreshTokenMissing(serverHttpRequest)) {
			refreshTokenCookie = cookies.get("refreshToken").get(0);
		}

		if (jwtUtil.isInvalid(accessTokenCookie.getValue())) {

			if (jwtUtil.isInvalid(refreshTokenCookie.getValue())) {
				return this.onError(exchange, HttpStatus.UNAUTHORIZED, accessTokenCookie,
					refreshTokenCookie);
			}

			String userId = jwtUtil.getUserId(refreshTokenCookie.getValue());

			WebClient webClient = WebClient.builder()
				.baseUrl(AUTH_SERVER_URL)
				.defaultHeader("InternalKey", INTERNAL_KEY)
				.build();

			TokenRes tokenRes = webClient.get().uri("/auth/token").header("UserId", userId)
				.retrieve().bodyToMono(new ParameterizedTypeReference<TokenRes>() {
				})
				.timeout(Duration.ofMillis(2000))
				.block();

			if (tokenRes == null) {
				return this.onError(exchange, HttpStatus.UNAUTHORIZED, accessTokenCookie,
					refreshTokenCookie);
			}

			updateResponse(exchange, tokenRes.getAccessToken(), tokenRes.getRefreshToken());
		}

		this.updateRequest(exchange, accessTokenCookie.getValue());

		return chain.filter(exchange);
	}

	private void updateRequest(ServerWebExchange exchange, String accessToken) {
		String userId = jwtUtil.getUserId(accessToken);
		exchange.getRequest().mutate().header("UserId", userId).build();
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

	private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus,
		HttpCookie... responseCookies) {
		ServerHttpResponse serverHttpResponse = exchange.getResponse();
		serverHttpResponse.setStatusCode(httpStatus);
		for (HttpCookie r : responseCookies) {
			exchange.getResponse().addCookie(ResponseCookie.from(r.getName(), r.getValue()).maxAge(0).build());
		}
		return serverHttpResponse.setComplete();
	}

	private boolean isAccessTokenMissing(ServerHttpRequest serverHttpRequest) {
		return !serverHttpRequest.getCookies().containsKey("accessToken");
	}

	private boolean isRefreshTokenMissing(ServerHttpRequest serverHttpRequest) {
		return !serverHttpRequest.getCookies().containsKey("refreshToken");
	}

}
