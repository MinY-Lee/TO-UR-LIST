package com.eminyidle.user.application.domain.model;

import java.time.LocalDateTime;
public class User {
    private String userId;
    private String userGoogleId;
    private String userNickname;
    private String userName;
    private LocalDateTime userBirth;
    private Integer userGender;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
