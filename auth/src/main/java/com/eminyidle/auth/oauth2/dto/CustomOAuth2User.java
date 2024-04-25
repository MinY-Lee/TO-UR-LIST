package com.eminyidle.auth.oauth2.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2Response oAuth2Response;
    @Getter
    private final String userId;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

      return new ArrayList<>();
    }

    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

    public String getUserEmail() {
        return oAuth2Response.getEmail();
    }

}
