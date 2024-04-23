package com.eminyidle.auth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String nickname;

    private Integer profileIconId;

    private Integer profileFrameId;

    private Integer themeId;

    private Integer titleId;
}
