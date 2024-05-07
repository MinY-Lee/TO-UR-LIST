package com.eminyidle.tour.dto.res;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRes {
    private String userId;
    private String userNickname;
    private String userName;

    private LocalDateTime userBirth;

    private Integer userGender;
}
