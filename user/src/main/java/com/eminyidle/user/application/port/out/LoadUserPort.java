package com.eminyidle.user.application.port.out;

import com.eminyidle.user.domain.User;

import java.util.List;

public interface LoadUserPort {
	User load(String userId);

	void loadByUserNickname(String userNickname);

	List<User> loadUserListByUserNickname(String userNickname);
}
