package com.eminyidle.user.application.service;

import com.eminyidle.user.application.port.in.CheckNicknameDuplicationUsecase;
import com.eminyidle.user.application.port.in.CreateUserUsecase;
import com.eminyidle.user.application.port.in.DeleteUserUsecase;
import com.eminyidle.user.application.port.in.SearchUserUsecase;
import com.eminyidle.user.application.port.in.UpdateUserUsecase;
import com.eminyidle.user.application.port.out.DeleteUserPort;
import com.eminyidle.user.application.port.out.LoadUserPort;
import com.eminyidle.user.application.port.out.SaveUserPort;
import com.eminyidle.user.domain.User;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements SearchUserUsecase, CreateUserUsecase, DeleteUserUsecase, UpdateUserUsecase,
	CheckNicknameDuplicationUsecase {

	private final LoadUserPort loadUserPort;
	private final SaveUserPort saveUserPort;
	private final DeleteUserPort deleteUserPort;

	@Override
	public Boolean checkNicknameDuplication(String userNickname) {
		try {
			loadUserPort.loadByUserNickname(userNickname);
			return false;
		} catch (NoSuchElementException e) {
			return true;
		}
	}

	@Override
	public void createUser(String userId, String userGoogleId, String userNickname, String userName,
		LocalDateTime userBirth, Integer userGender) {

		User user = User.builder()
			.userId(userId)
			.userGoogleId(userGoogleId)
			.userNickname(userNickname)
			.userName(userName)
			.userBirth(userBirth)
			.userGender(userGender)
			.build();

		saveUserPort.save(user);

	}

	@Override
	public void deleteUser(String userId) {
		deleteUserPort.delete(userId);
	}

	@Override
	public void updateUserNickname(String userId, String userNickname) {
		User user = loadUserPort.load(userId);

		user.setUserNickname(userNickname);
		saveUserPort.save(user);
	}

	@Override
	public void updateUserName(String userId, String userName) {
		User user = loadUserPort.load(userId);

		user.setUserName(userName);
		saveUserPort.save(user);
	}

	@Override
	public void updateUserBirth(String userId, LocalDateTime userBirth) {
		User user = loadUserPort.load(userId);

		user.setUserBirth(userBirth);
		saveUserPort.save(user);

	}

	@Override
	public void updateUserGender(String userId, Integer userGender) {
		User user = loadUserPort.load(userId);

		user.setUserGender(userGender);
		saveUserPort.save(user);

	}

	@Override
	public User searchUser(String userId) {
		return loadUserPort.load(userId);
	}
}
