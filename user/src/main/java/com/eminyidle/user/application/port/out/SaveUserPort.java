package com.eminyidle.user.application.port.out;

import com.eminyidle.user.domain.User;

public interface SaveUserPort {
	void save(User user);
}
