package com.eminyidle.user.application.port.in;

import com.eminyidle.user.domain.User;

public interface SearchUserUsecase {
	User searchUser(String userId);
}
