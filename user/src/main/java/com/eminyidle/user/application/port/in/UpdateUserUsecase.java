package com.eminyidle.user.application.port.in;

import com.eminyidle.user.domain.User;
import java.time.LocalDateTime;

public interface UpdateUserUsecase {
	void updateUserNickname(String userId, String userNickname);

	void updateUserName(String userId, String userName);

	void updateUserBirth(String userId, LocalDateTime userBirth);

	void updateUserGender(String userId, Integer userGender);
}
