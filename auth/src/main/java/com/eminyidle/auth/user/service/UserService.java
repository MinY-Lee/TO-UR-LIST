package com.eminyidle.auth.user.service;

import com.eminyidle.auth.user.dto.User;
import com.eminyidle.auth.user.dto.req.UpdateUserNameReq;
import com.eminyidle.auth.user.dto.req.UpdateUserProfileFrameReq;
import com.eminyidle.auth.user.dto.req.UpdateUserProfileIconReq;
import com.eminyidle.auth.user.dto.req.UpdateUserThemeReq;
import com.eminyidle.auth.user.exception.TitleNotExistException;
import java.net.URISyntaxException;

public interface UserService {

    void saveUserInfo(User user);

    void updateUserName(UpdateUserNameReq updateUserNameReq, User user);

    void updateUserTheme(UpdateUserThemeReq updateUserThemeReq, User user);

    void updateUserProfileIcon(UpdateUserProfileIconReq updateUserProfileIconReq, User user);

    void updateUserProfileFrame(UpdateUserProfileFrameReq updateUserProfileFrameReq, User user);

    void updateUserTitle(Integer titleId, String userId) throws TitleNotExistException;

    void deleteUser(User user) throws URISyntaxException;

    User findTwn(String email);
}
