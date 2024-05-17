package com.eminyidle.user.application.service;

import com.eminyidle.user.application.port.in.CheckNicknameDuplicationUsecase;
import com.eminyidle.user.application.port.in.CreateUserUsecase;
import com.eminyidle.user.application.port.in.DeleteUserUsecase;
import com.eminyidle.user.application.port.in.SearchUserUsecase;
import com.eminyidle.user.application.port.in.UpdateUserUsecase;
import com.eminyidle.user.application.port.out.DeleteUserAlertPort;
import com.eminyidle.user.application.port.out.DeleteUserPort;
import com.eminyidle.user.application.port.out.DeleteUserRequestPort;
import com.eminyidle.user.application.port.out.LoadUserPort;
import com.eminyidle.user.application.port.out.SaveUserPort;
import com.eminyidle.user.application.port.out.UpdateUserAlertPort;
import com.eminyidle.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements SearchUserUsecase, CreateUserUsecase, DeleteUserUsecase,
	UpdateUserUsecase,
	CheckNicknameDuplicationUsecase {

	private final LoadUserPort loadUserPort;
	private final SaveUserPort saveUserPort;
	private final DeleteUserPort deleteUserPort;
	private final UpdateUserAlertPort updateUserPort;
	private final DeleteUserRequestPort deleteUserRequestPort;
	private final DeleteUserAlertPort deleteUserAlertPort;

	@Override
	public Boolean checkNicknameDuplication(String userNickname) {
		// isDuplicated
		// 중복시 true 반환
		User user = loadUserPort.loadByUserNickname(userNickname);
		return user.getUserId() != null;
	}

	@Override
	public void createUser(String userId, String userNickname, String userName,
		LocalDateTime userBirth, Integer userGender) {

		User user = User.builder()
			.userId(userId)
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
		deleteUserRequestPort.deleteUserRequest(userId);
		deleteUserAlertPort.deleteUserAlertSend(User.builder().userId(userId).build());
	}

	@Override
	public void updateUserNickname(String userId, String userNickname) {
		User user = loadUserPort.load(userId);

		user.setUserNickname(userNickname);
		saveUserPort.save(user);
		updateUserPort.updateUserAlertSend(user);
	}

	@Override
	public void updateUserName(String userId, String userName) {
		User user = loadUserPort.load(userId);

		user.setUserName(userName);
		saveUserPort.save(user);
		updateUserPort.updateUserAlertSend(user);
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

	@Override
	public List<User> searchUserList(String userNickname) {
		return loadUserPort.loadUserListByUserNickname(userNickname);
	}
}
