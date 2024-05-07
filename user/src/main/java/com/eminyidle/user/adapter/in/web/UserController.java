package com.eminyidle.user.adapter.in.web;

import com.eminyidle.user.adapter.dto.req.CreateUserReq;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final CheckNicknameDuplicationUsecase checkNicknameDuplicationUsecase;
	private final CreateUserUsecase createUserUsecase;
	private final DeleteUserUsecase deleteUserUsecase;
	private final SearchUserUsecase searchUserUsecase;
	private final UpdateUserUsecase updateUserUsecase;

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestHeader("UserId") String userId, @RequestBody CreateUserReq createUserReq) {
		log.info("userId: {}", userId);
		createUserUsecase.createUser(
			userId, createUserReq.getUserNickname(), createUserReq.getUserName(),
			createUserReq.getUserBirth(), createUserReq.getUserGender());


		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<GetUserDetailRes> getUserDetail(@RequestHeader("UserId") String userId) {
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
	public ResponseEntity<Void> deleteUser(@RequestHeader("UserId") String userId) {
		deleteUserUsecase.deleteUser(userId);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/name")
	public ResponseEntity<Void> updateUserName(@RequestHeader("UserId") String userId, @RequestBody
	UpdateUserNameReq updateUserNameReq) {
		updateUserUsecase.updateUserName(userId, updateUserNameReq.getUserName());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/nickname")
	public ResponseEntity<Void> updateUserNickname(@RequestHeader("UserId") String userId, @RequestBody
	UpdateUserNicknameReq updateUserNicknameReq) {
		updateUserUsecase.updateUserNickname(userId, updateUserNicknameReq.getUserNickname());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/birth")
	public ResponseEntity<Void> updateUserBirth(@RequestHeader("UserId") String userId, @RequestBody
	UpdateUserBirthReq updateUserBirthReq) {
		updateUserUsecase.updateUserBirth(userId, updateUserBirthReq.getUserBirth());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/gender")
	public ResponseEntity<Void> updateUserGender(@RequestHeader("UserId") String userId, @RequestBody
	UpdateUserGenderReq updateUserGenderReq) {
		updateUserUsecase.updateUserGender(userId, updateUserGenderReq.getUserGender());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/nickname/{userNickname}")
	public ResponseEntity<CheckNicknameDuplicationRes> checkNicknameDuplication(
		@PathVariable(value="userNickname") String userNickname) {
		Boolean result = checkNicknameDuplicationUsecase.checkNicknameDuplication(userNickname);
		return ResponseEntity.ok()
			.body(CheckNicknameDuplicationRes.builder().isDuplicated(result).build());

	}

}
