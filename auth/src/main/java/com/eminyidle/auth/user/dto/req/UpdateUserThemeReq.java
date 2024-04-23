package com.eminyidle.auth.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 테마 정보 수정 시에 사용할 RequestBody
 */
@NoArgsConstructor
@Getter
public class UpdateUserThemeReq {

    @NotBlank
    private Integer themeId;
}