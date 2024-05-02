package com.eminyidle.user.adapter.dto.res;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetUserDetailRes {
	private String userId;
	private String userNickname;
	private String userName;
	private LocalDateTime userBirth;
	private Integer userGender;

}
