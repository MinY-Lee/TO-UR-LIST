package com.eminyidle.auth.oauth2.service;

import com.eminyidle.auth.oauth2.dto.CustomOAuth2User;
import com.eminyidle.auth.oauth2.dto.GoogleResponse;
import com.eminyidle.auth.oauth2.dto.OAuth2AuthorizedClient;
import com.eminyidle.auth.oauth2.dto.OAuth2Response;
import com.eminyidle.auth.oauth2.dto.Userinfo;
import com.eminyidle.auth.oauth2.exception.UserNotExistException;
import com.eminyidle.auth.oauth2.repository.GoogleRepository;
import com.eminyidle.auth.oauth2.repository.UserinfoRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    //DefaultOAuth2UserService는 OAuth2UserService의 구현체

    private final UserinfoRepository userinfoRepository;

    /**
     * 네이버나 구글의 사용자 정보를 파라미터로 받아오는 메서드
     * DB에 초기 회원 조회 로직이 들어감
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String userGoogleId = oAuth2Response.getProviderId();
        Optional<Userinfo> existData = userinfoRepository.findById(userGoogleId);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User();
        customOAuth2User.setUserEmail(oAuth2Response.getEmail());
        customOAuth2User.setName(oAuth2Response.getName());

        if(existData.isEmpty()) {
            Userinfo newUser = new Userinfo();
            newUser.setUserGoogleId(userGoogleId);
            userinfoRepository.save(newUser);
            customOAuth2User.setUserId(newUser.getUserId());
        } else {
            customOAuth2User.setUserId(existData.get().getUserId());
        }

        return customOAuth2User;
    }
}
