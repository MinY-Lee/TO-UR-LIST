package com.eminyidle.user.application.port.out;

import com.eminyidle.user.domain.User;
import java.util.Optional;

public interface LoadUserPort {
	User load(String userId);

	User loadByUserNickname(String userNickname);
}
