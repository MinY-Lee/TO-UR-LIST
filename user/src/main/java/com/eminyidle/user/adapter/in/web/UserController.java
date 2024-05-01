package com.eminyidle.user.adapter.in.web;

import com.eminyidle.user.adapter.dto.req.UpdateUserBirthReq;
import com.eminyidle.user.adapter.dto.req.UpdateUserGenderReq;
import com.eminyidle.user.adapter.dto.req.UpdateUserNameReq;
import com.eminyidle.user.adapter.dto.req.UpdateUserNicknameReq;
import com.eminyidle.user.adapter.dto.res.CheckNicknameDuplicationRes;
import com.eminyidle.user.adapter.dto.res.GetUserDetailRes;
import com.eminyidle.user.application.port.in.CheckNicknameDuplicationUsecase;
import com.eminyidle.user.application.port.in.CreateUserUsecase;
import com.eminyidle.user.application.port.in.DeleteUserUsecase;
import com.eminyidle.user.application.port.in.SearchUserUsecase;
import com.eminyidle.user.application.port.in.UpdateUserUsecase;
import com.eminyidle.user.application.service.UserService;
import com.eminyidle.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final CheckNicknameDuplicationUsecase checkNicknameDuplicationUsecase;
	private final CreateUserUsecase createUserUsecase;
	private final DeleteUserUsecase deleteUserUsecase;
	private final SearchUserUsecase searchUserUsecase;
	private final UpdateUserUsecase updateUserUsecase;

	@GetMapping
	public ResponseEntity<GetUserDetailRes> getUserDetail(@Header("UserId") String userId) {
		User user = searchUserUsecase.searchUser(userId);

		return ResponseEntity.ok().body(GetUserDetailRes.builder()
			.userId(user.getUserId())
			.userNickname(user.getUserNickname())
			.userName(user.getUserName())
			.userBirth(user.getUserBirth())
			.userGender(user.getUserGender())
			.build());
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteUser(@Header("UserId") String userId) {
		deleteUserUsecase.deleteUser(userId);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/name")
	public ResponseEntity<Void> updateUserName(@Header("UserId") String userId, @RequestBody
	UpdateUserNameReq updateUserNameReq) {
		updateUserUsecase.updateUserName(userId, updateUserNameReq.getUserName());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/nickname")
	public ResponseEntity<Void> updateUserNickname(@Header("UserId") String userId, @RequestBody
	UpdateUserNicknameReq updateUserNicknameReq) {
		updateUserUsecase.updateUserNickname(userId, updateUserNicknameReq.getUserName());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/birth")
	public ResponseEntity<Void> updateUserBirth(@Header("UserId") String userId, @RequestBody
	UpdateUserBirthReq updateUserBirthReq) {
		updateUserUsecase.updateUserBirth(userId, updateUserBirthReq.getUserBirth());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/gender")
	public ResponseEntity<Void> updateUserGender(@Header("UserId") String userId, @RequestBody
	UpdateUserGenderReq updateUserGenderReq) {
		updateUserUsecase.updateUserGender(userId, updateUserGenderReq.getUserGender());
		return ResponseEntity.ok().build();
	}

	@GetMapping("nickname/{userNickname}")
	public ResponseEntity<CheckNicknameDuplicationRes> checkNicknameDuplication(
		@PathVariable String userNickname) {
		Boolean result = checkNicknameDuplicationUsecase.checkNicknameDuplication(userNickname);
		return ResponseEntity.ok()
			.body(CheckNicknameDuplicationRes.builder().isDuplicated(result).build());

	}

}
