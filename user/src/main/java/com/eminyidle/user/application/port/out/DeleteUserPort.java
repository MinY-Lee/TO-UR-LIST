package com.eminyidle.user.application.port.out;

import com.eminyidle.user.domain.User;

public interface DeleteUserPort {
	void delete(String userId);
}
