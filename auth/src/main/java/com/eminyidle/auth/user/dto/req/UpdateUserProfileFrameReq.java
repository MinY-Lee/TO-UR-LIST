package com.eminyidle.auth.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프로필 프레임 정보 수정 시에 사용할 RequestBody
 */
@NoArgsConstructor
@Getter
public class UpdateUserProfileFrameReq {

    @NotBlank
    private Integer profileFrameId;
}