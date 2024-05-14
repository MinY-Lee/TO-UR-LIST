package com.eminyidle.user.adapter.dto.res;

import com.eminyidle.user.adapter.dto.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetUserInfoListRes {
    private List<UserInfo> userInfoList;
}
