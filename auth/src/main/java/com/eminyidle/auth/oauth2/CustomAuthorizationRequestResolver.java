package com.eminyidle.auth.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * Refresh Token을 로그인 할때마다 가져오게 하기 위한 커스텀 요청리졸버
 * 구글에서는 Refresh Token을 한번만 생성하고 관리한다.
 */
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = this.defaultResolver.resolve(request);
        return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest) : null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = this.defaultResolver.resolve(request, clientRegistrationId);
        return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest) : null;
    }

    private OAuth2AuthorizationRequest customAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
        Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());
        additionalParameters.put("access_type", "offline");
        additionalParameters.put("approval_prompt", "force");

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .build();
    }
}
