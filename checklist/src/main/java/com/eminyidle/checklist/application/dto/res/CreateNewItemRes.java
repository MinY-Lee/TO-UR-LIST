package com.eminyidle.checklist.application.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewItemRes {
    private Boolean isDuplicated;
}
