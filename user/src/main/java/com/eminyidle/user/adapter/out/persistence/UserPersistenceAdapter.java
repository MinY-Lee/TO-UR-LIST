package com.eminyidle.user.adapter.out.persistence;

import com.eminyidle.user.application.port.out.LoadUserPort;
import com.eminyidle.user.application.port.out.SaveUserPort;
import com.eminyidle.user.domain.User;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public User load(String userId) {
		UserEntity userEntity = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

		return userMapper.toDomain(userEntity);
	}

	@Override
	public User loadByUserNickname(String userNickname) {
		UserEntity userEntity = userRepository.findByUserNickname(userNickname).orElseThrow(NoSuchElementException::new);

		return userMapper.toDomain(userEntity);
	}

	@Override
	public void save(User user) {
		userRepository.save(userMapper.toEntity(user));
	}
}
