package com.eminyidle.user.application.port.out;

import com.eminyidle.user.domain.User;

public interface DeleteUserAlertPort {
	void deleteUserAlertSend(User user);
}
