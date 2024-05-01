package com.eminyidle.user.adapter.dto.req;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserReq {

	private String userNickname;
	private String userName;
	private LocalDateTime userBirth;
	private Integer userGender;

}
