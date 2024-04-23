package com.eminyidle.auth.user.dto.res;

import com.eminyidle.auth.user.dto.PreviousPlayInfo;
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
public class SearchPreviousPlayInfoRes {
    private Boolean isExist;
    private PreviousPlayInfo previousPlayInfo;
}
