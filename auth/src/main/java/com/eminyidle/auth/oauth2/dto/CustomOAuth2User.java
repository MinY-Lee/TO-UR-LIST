package com.eminyidle.auth.oauth2.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
@Getter
@Setter
public class CustomOAuth2User implements OAuth2User {

    private String userId;
    private String name;
    private String userEmail;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

      return new ArrayList<>();
    }

}
