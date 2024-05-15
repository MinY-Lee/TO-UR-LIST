package com.eminyidle.gateway.config;

import com.eminyidle.gateway.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

	private final AuthenticationFilter authenticationFilter;

	@Value("${USER_SERVER_URL}")
	private String USER_SERVER_URL;

	@Value("${TOUR_SERVER_URL}")
	private String TOUR_SERVER_URL;

	@Value("${PLACE_SERVER_URL}")
	private String PLACE_SERVER_URL;

	@Value("${PAYMENT_SERVER_URL}")
	private String PAYMENT_SERVER_URL;

	@Value("${FEED_SERVER_URL}")
	private String FEED_SERVER_URL;

	@Value("${CHECKLIST_SERVER_URL}")
	private String CHECKLIST_SERVER_URL;

	@Value("${CHECKLIST_SERVER_URL}")
	private String WEBSOCKET_PLACE_SERVER;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
			.route("USER_SERVER", predicateSpec -> predicateSpec.path("/user/**")
				.filters(gatewayFilterSpec -> gatewayFilterSpec.filter(authenticationFilter))
				.uri(USER_SERVER_URL))
			.route("TOUR_SERVER", predicateSpec -> predicateSpec.path("/tour/**")
				.filters(gatewayFilterSpec -> gatewayFilterSpec.filter(authenticationFilter))
				.uri(TOUR_SERVER_URL))
			.route("TOUR_SERVER", predicateSpec -> predicateSpec.path("/country/**")
				.filters(gatewayFilterSpec -> gatewayFilterSpec.filter(authenticationFilter))
				.uri(TOUR_SERVER_URL))
			.route("PLACE_SERVER", predicateSpec -> predicateSpec.path("/place/**")
				.filters(gatewayFilterSpec -> gatewayFilterSpec.filter(authenticationFilter))
				.uri(PLACE_SERVER_URL))
			.route("PAYMENT_SERVER", predicateSpec -> predicateSpec.path("/payment/**")
				.filters(gatewayFilterSpec -> gatewayFilterSpec.filter(authenticationFilter))
				.uri(PAYMENT_SERVER_URL))
			.route("FEED_SERVER", predicateSpec -> predicateSpec.path("/feed/**")
				.filters(gatewayFilterSpec -> gatewayFilterSpec.filter(authenticationFilter))
				.uri(FEED_SERVER_URL))
			.route("CHECKLIST_SERVER", predicateSpec -> predicateSpec.path("/checklist/**")
				.filters(gatewayFilterSpec -> gatewayFilterSpec.filter(authenticationFilter))
				.uri(CHECKLIST_SERVER_URL))
			.route("WEBSOCKET_PLACE_SERVER", predicateSpec -> predicateSpec.path("/ws/place/**")
				.filters(f -> f.rewritePath("/ws/(?<remaining>.*)", "/${remaining}")
					.filter(authenticationFilter))
				.uri(WEBSOCKET_PLACE_SERVER))
			.build();
	}
}