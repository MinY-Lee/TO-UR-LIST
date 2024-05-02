package com.eminyidle.gateway.filter;


import com.eminyidle.gateway.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

	private final JWTUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest serverHttpRequest = exchange.getRequest();

		if (this.isAccessTokenMissing(serverHttpRequest)) {
			return this.onError(exchange, HttpStatus.UNAUTHORIZED);
		}

		String accessToken = serverHttpRequest.getCookies().get("accessToken").get(0).getValue();

		if (jwtUtil.isInvalid(accessToken)) {
			return this.onError(exchange, HttpStatus.UNAUTHORIZED);
		}

		this.updateRequest(exchange, accessToken);

		return chain.filter(exchange);
	}

	private void updateRequest(ServerWebExchange exchange, String accessToken) {
		String userId = jwtUtil.getUserId(accessToken);
		exchange.getRequest().mutate().header("UserId", userId).build();
	}

	private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
		ServerHttpResponse serverHttpResponse = exchange.getResponse();
		serverHttpResponse.setStatusCode(httpStatus);
		return serverHttpResponse.setComplete();
	}

	private boolean isAccessTokenMissing(ServerHttpRequest serverHttpRequest) {
		return !serverHttpRequest.getCookies().containsKey("accessToken");
	}

}
