package com.eminyidle.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class InternalKeyFilter extends OncePerRequestFilter {

	private final String INTERNAL_KEY;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String requestUri = request.getRequestURI();
		if (!requestUri.startsWith("/auth/token") || !requestUri.startsWith("/auth/user")) {
			filterChain.doFilter(request, response);
			return;
		}

		String headerInternalKey = request.getHeader("InternalKey");
		if(headerInternalKey != null && headerInternalKey.equals(INTERNAL_KEY)) {
			filterChain.doFilter(request, response);
			return;
		}

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
