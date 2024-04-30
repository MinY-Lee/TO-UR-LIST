package com.eminyidle.user.application.port.out;

import com.eminyidle.user.domain.User;

public interface LoadUserPort {
	User load(String userId);

	void loadByUserNickname(String userNickname);
}
