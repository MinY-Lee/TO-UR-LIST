package com.eminyidle.checklist.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewItemRes {
    private Boolean isDuplicated;
}
