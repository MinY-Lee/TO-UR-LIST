package com.eminyidle.auth.oauth2.service;

import com.eminyidle.auth.oauth2.dto.CustomOAuth2User;
import com.eminyidle.auth.oauth2.dto.GoogleResponse;
import com.eminyidle.auth.oauth2.dto.OAuth2Response;
import com.eminyidle.auth.resource.dto.ProfileFrame;
import com.eminyidle.auth.resource.dto.ProfileIcon;
import com.eminyidle.auth.resource.dto.Theme;
import com.eminyidle.auth.resource.dto.Title;
import com.eminyidle.auth.resource.repository.ProfileFrameRepository;
import com.eminyidle.auth.resource.repository.ProfileIconRepository;
import com.eminyidle.auth.resource.repository.ThemeRepository;
import com.eminyidle.auth.resource.repository.TitleRepository;
import com.eminyidle.auth.user.dto.*;
import com.eminyidle.auth.user.repository.*;
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

    private final UserRepository userRepository;

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

        // 구글에서 아이디 추출
        String userGoogleId = oAuth2Response.getProviderId();
        Optional<User> existData = userRepository.findByUserGoogleId(userGoogleId);
        // 구글에서 이메일 추출
        String userEmail = oAuth2Response.getEmail();
//        Optional<User> existData = userRepository.findByUserEmailAndDeletedAtIsNull(userEmail);

        //회원가입(첫 로그인)
        if (existData.isEmpty()) {
            // 초기 프레임, 초기 아이콘 설정
            Optional<ProfileIcon> profileIcon = profileIconRepository.findById(1); // ID에 해당하는 ProfileIcon 조회
            Optional<ProfileFrame> profileFrame = profileFrameRepository.findById(1); // ID에 해당하는 ProfileFrame 조회
            Optional<Theme> theme = themeRepository.findById(1);
            Optional<Title> title = titleRepository.findById(1);

            User user = User.builder()
                    .userGoogleId(oAuth2Response.getProviderId())
                    .userEmail(oAuth2Response.getEmail())
                    .userGoogleName(oAuth2Response.getName())
                    .userNickname(oAuth2Response.getName())
                    .profileIcon(profileIcon.get()) // 조회한 ProfileIcon 사용
                    .profileFrame(profileFrame.get()) // 조회한 ProfileFrame 사용
                    .theme(theme.get())
                    .title(title.get())
                    .career(0)
                    .build();

            userRepository.save(user);
        }
        //탈퇴한 회원
        else if (existData.get().getDeletedAt() != null) {
            User userToUpdate = existData.get();
            userToUpdate.setDeletedAt(null); // 업데이트할 값 설정
            userRepository.save(userToUpdate);
        }

        return new CustomOAuth2User(oAuth2Response);
    }
}
