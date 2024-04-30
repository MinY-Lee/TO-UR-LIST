package com.eminyidle.user.adapter.out.persistence;

import com.eminyidle.user.adapter.dto.UserEntity;
import com.eminyidle.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	public User toDomain(UserEntity userEntity) {
		return User.builder()
			.userId(userEntity.getUserId())
			.userGoogleId(userEntity.getUserGoogleId())
			.userNickname(userEntity.getUserNickname())
			.userName(userEntity.getUserName())
			.userGender(userEntity.getUserGender())
			.userBirth(userEntity.getUserBirth())
			.build();
	}

	public UserEntity toEntity(User user) {
		return UserEntity.builder()
			.userId(user.getUserId())
			.userGoogleId(user.getUserGoogleId())
			.userNickname(user.getUserNickname())
			.userName(user.getUserName())
			.userGender(user.getUserGender())
			.userBirth(user.getUserBirth())
			.build();
	}
}
