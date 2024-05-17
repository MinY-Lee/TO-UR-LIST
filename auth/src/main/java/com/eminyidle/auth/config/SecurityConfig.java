package com.eminyidle.auth.config;

import com.eminyidle.auth.filter.InternalKeyFilter;
import com.eminyidle.auth.jwt.JWTFilter;
import com.eminyidle.auth.jwt.JWTUtil;
import com.eminyidle.auth.oauth2.CustomAuthorizationRequestResolver;
import com.eminyidle.auth.oauth2.CustomClientRegistrationRepo;
import com.eminyidle.auth.oauth2.OAuth2MemberFailureHandler;
import com.eminyidle.auth.oauth2.OAuth2MemberSuccessHandler;
import com.eminyidle.auth.oauth2.repository.UserinfoRepository;
import com.eminyidle.auth.oauth2.service.CustomOAuth2AuthorizedClientService;
import com.eminyidle.auth.oauth2.service.CustomOAuth2UserService;
import com.eminyidle.auth.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

	private final JWTUtil jwtUtil;
	private final UserinfoRepository userinfoRepository;
	private final RedisService redisService;
	private final CustomOAuth2UserService customOAuth2UserService; // OAuth2UserService가 정의된 서비스
	private final CustomClientRegistrationRepo customClientRegistrationRepo;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final ClientRegistrationRepository clientRegistrationRepository;
	private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
	private final JdbcTemplate jdbcTemplate;

	// 로그인 후 이동할 URL
	@Value("${auth-redirect-url}")
	String mainPage;

	@Value("${INTERNAL_KEY}")
	String internalKey;

	//AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
		throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// csrf 보호는 세션 기반 인증에 활용하는 것을 추천
			.csrf(CsrfConfigurer::disable)
			.formLogin(FormLoginConfigurer::disable)
			.httpBasic(HttpBasicConfigurer::disable);

		// 소셜 로그인 등록
		http
			.oauth2Login(oauth2 -> oauth2
				.successHandler(new OAuth2MemberSuccessHandler(jwtUtil, redisService, mainPage))
				.failureHandler(new OAuth2MemberFailureHandler())
				.authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
					.authorizationRequestResolver(
						new CustomAuthorizationRequestResolver(clientRegistrationRepository))
				)
				.clientRegistrationRepository(
					customClientRegistrationRepo.clientRegistrationRepository())
				.authorizedClientService(
					customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate,
						customClientRegistrationRepo.clientRegistrationRepository()))
				.userInfoEndpoint(
					userInfoEndpointConfig -> userInfoEndpointConfig //data를 받을 수 있는 UserDetailsService를 등록해주는 endpoint
						.userService(customOAuth2UserService)))
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.authenticationEntryPoint(authenticationEntryPoint)
			);

		// 인증 경로 설정
		http
			.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll());

		//JWTFilter 등록
		http
			.addFilterAfter(new InternalKeyFilter(internalKey),
				UsernamePasswordAuthenticationFilter.class);

		// session 설정
		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedMethod("PATCH");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


}
