package com.eminyidle.auth.user.controller;

import com.eminyidle.auth.auth.cookie.CookieUtil;
import com.eminyidle.auth.user.dto.AchieveInfo;
import com.eminyidle.auth.user.dto.PreviousPlayInfo;
import com.eminyidle.auth.user.dto.User;
import com.eminyidle.auth.user.dto.req.UpdateUserNameReq;
import com.eminyidle.auth.user.dto.req.UpdateUserProfileFrameReq;
import com.eminyidle.auth.user.dto.req.UpdateUserProfileIconReq;
import com.eminyidle.auth.user.dto.req.UpdateUserThemeReq;
import com.eminyidle.auth.user.dto.res.SearchAchieveRes;
import com.eminyidle.auth.user.dto.res.SearchHonorRes;
import com.eminyidle.auth.user.dto.res.SearchPreviousPlayInfoRes;
import com.eminyidle.auth.user.dto.res.SearchUserInfoRes;
import com.eminyidle.auth.user.service.AchieveService;
import com.eminyidle.auth.user.service.GameInfoService;
import com.eminyidle.auth.user.service.HonorService;
import com.eminyidle.auth.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final HonorService honorService;
    private final AchieveService achieveService;
    private final GameInfoService gameInfoService;

    @PutMapping("/nickname")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserNameReq updateUserReq,
                                           @AuthenticationPrincipal User user) {
        userService.updateUserName(updateUserReq, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/theme")
    public ResponseEntity<Void> updateTheme(@RequestBody UpdateUserThemeReq updateUserThemeReq,
                                            @AuthenticationPrincipal User user) {
        userService.updateUserTheme(updateUserThemeReq, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile-icon")
    public ResponseEntity<Void> updateProfileIcon(@RequestBody UpdateUserProfileIconReq updateUserProfileIconReq,
                                                  @AuthenticationPrincipal User user) {
        userService.updateUserProfileIcon(updateUserProfileIconReq, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile-frame")
    public ResponseEntity<Void> updateProfileFrame(@RequestBody UpdateUserProfileFrameReq updateUserProfileFrameReq,
                                                   @AuthenticationPrincipal User user) {
        userService.updateUserProfileFrame(updateUserProfileFrameReq, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/title")
    public ResponseEntity<SearchHonorRes> searchHonor(@AuthenticationPrincipal User user) {
        List<Integer> honorList = honorService.searchAllHonor(user.getUserId());

        return ResponseEntity.ok()
                .body(
                        SearchHonorRes.builder()
                                .acquiredTitleList(honorList)
                                .build()
                );
    }

    @GetMapping("/achievement")
    public ResponseEntity<SearchAchieveRes> searchAchievement(@AuthenticationPrincipal User user) {
        List<AchieveInfo> achievementList = achieveService.searchAllAchieve(user.getUserId());

        return ResponseEntity.ok()
                .body(
                        SearchAchieveRes.builder()
                                .acquiredAchievementList(achievementList)
                                .build()
                );
    }

    @GetMapping("/previous")
    public ResponseEntity<SearchPreviousPlayInfoRes> searchGameInfo(@AuthenticationPrincipal User user) {
        PreviousPlayInfo previousPlayInfo = gameInfoService.searchPreviousPlayInfo(user.getUserId());

        return ResponseEntity.ok().
                body(
                        SearchPreviousPlayInfoRes.builder()
                                .isExist(!previousPlayInfo.getPreviousPlayDate().isEmpty())
                                .previousPlayInfo(previousPlayInfo)
                                .build()
                );
    }

    @DeleteMapping("/new-game")
    public void resetGame(@AuthenticationPrincipal User user) {
        gameInfoService.startNewGame(user.getUserId());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @AuthenticationPrincipal User user) throws URISyntaxException {
        userService.deleteUser(user);
        CookieUtil.deleteTokenCookie(request, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<SearchUserInfoRes> searchUserInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok()
                .body(
                        SearchUserInfoRes.builder()
                                .userNickname(user.getUserNickname())
                                .profileIconId(user.getProfileIcon().getProfileIconId())
                                .profileFrameId(user.getProfileFrame().getProfileFrameId())
                                .themeId(user.getTheme().getThemeId())
                                .titleId(user.getTitle().getTitleId())
                                .career(user.getCareer())
                                .build()
                );
    }

}
