package com.eminyidle.tour.adapter.in.messaging.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserNode {
    private String userId;
    private String userNickname;
    private String userName;
    private LocalDateTime userBirth;
    private Integer userGender;
}
