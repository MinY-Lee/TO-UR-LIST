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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort, DeleteUserPort {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public User load(String userId) {
		UserEntity userEntity = userRepository.findByUserIdAndDeletedAtIsNull(userId).orElseThrow(NoSuchElementException::new);

		return userMapper.toDomain(userEntity);
	}

	@Override
	public void loadByUserNickname(String userNickname) {
		UserEntity userEntity = userRepository.findByUserNicknameAndDeletedAtIsNull(userNickname).orElseThrow(NoSuchElementException::new);

		userMapper.toDomain(userEntity);
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
