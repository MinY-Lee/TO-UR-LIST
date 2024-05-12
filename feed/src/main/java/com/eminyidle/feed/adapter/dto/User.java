package com.eminyidle.feed.adapter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class User {
    private String userNickname;
    private String userName;
    private LocalDateTime userBirth;
    private Integer userGender;
    private String userProfileImageId;
}
