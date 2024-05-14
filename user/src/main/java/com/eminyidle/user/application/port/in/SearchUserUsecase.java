package com.eminyidle.user.application.port.in;

import com.eminyidle.user.domain.User;

import java.util.List;

public interface SearchUserUsecase {
	User searchUser(String userId);

	List<User> searchUserList(String userNickname);
}
