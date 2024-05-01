package com.eminyidle.user.application.port.in;

import java.time.LocalDateTime;

public interface CreateUserUsecase {
	void createUser(String userId, String userNickname, String userName, LocalDateTime userBirth, Integer userGender);
}
