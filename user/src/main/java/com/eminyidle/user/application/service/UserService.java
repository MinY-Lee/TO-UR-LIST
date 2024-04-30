package com.eminyidle.user.application.service;

import com.eminyidle.user.application.port.in.CheckNicknameDuplicationUsecase;
import com.eminyidle.user.application.port.in.CreateUserUsecase;
import com.eminyidle.user.application.port.in.DeleteUserUsecase;
import com.eminyidle.user.application.port.in.UpdateUserUsecase;
import com.eminyidle.user.application.port.out.LoadUserPort;
import com.eminyidle.user.application.port.out.SaveUserPort;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements CreateUserUsecase, DeleteUserUsecase, UpdateUserUsecase,
	CheckNicknameDuplicationUsecase {

	private final LoadUserPort loadUserPort;
	private final SaveUserPort saveUserPort;

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

	}

	@Override
	public void deleteUser(String userId) {

	}

	@Override
	public void updateUserNickname(String userId, String userNickname) {

	}

	@Override
	public void updateUserName(String userId, String userName) {

	}

	@Override
	public void updateUserBirth(String userId, LocalDateTime userBirth) {

	}

	@Override
	public void updateUserGender(String userId, Integer userGender) {

	}
}
