package com.eminyidle.user.adapter.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckNicknameDuplicationRes {

	private Boolean isDuplicated;

}
