package com.eminyidle.user.adapter.out.persistence;

import com.eminyidle.user.adapter.dto.UserEntity;
import com.eminyidle.user.application.port.out.DeleteUserPort;
import com.eminyidle.user.application.port.out.LoadUserPort;
import com.eminyidle.user.application.port.out.SaveUserPort;
import com.eminyidle.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort, DeleteUserPort {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public User loadActiveUser(String userId) {
		UserEntity userEntity = userRepository.findByUserIdAndDeletedAtIsNull(userId).orElseGet(UserEntity::new);
		return userMapper.toDomain(userEntity);
	}

	@Override
	public User loadByUserNickname(String userNickname) {
		UserEntity userEntity = userRepository.findByUserNickname(userNickname).orElse(
			UserEntity.builder().build());

		return userMapper.toDomain(userEntity);
	}

	@Override
	public List<User> loadUserListByUserNickname(String userNickname) {
		List<UserEntity> userEntityList = userRepository.findByUserNicknameContaining(userNickname);
        return userEntityList.stream()
				.map(userMapper::toDomain)
				.toList();
	}

	@Override
	public void save(User user) {
		UserEntity userEntity = userMapper.toEntity(user);
		userRepository.save(userEntity);
	}

	@Override
	public void delete(String userId) {
		UserEntity userEntity = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
		userEntity.setDeletedAt(LocalDateTime.now());
	}
}
