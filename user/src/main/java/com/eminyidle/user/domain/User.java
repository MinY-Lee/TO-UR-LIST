package com.eminyidle.user.domain;

import com.eminyidle.user.exception.InvalidGenderException;
import com.eminyidle.user.exception.InvalidUserNicknameException;
import com.eminyidle.user.exception.InvalidUsernameException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class User {
    private String userId;
    private String userNickname;
    private String userName;

    @Setter
    private LocalDateTime userBirth;

    private Integer userGender;

    public void setUserNickname(String userNickname) {
        if(userNickname == null || userNickname.isBlank()) {
            throw new InvalidUserNicknameException("user nickname is blank");
        } else if(userNickname.length() < 2) {
            throw new InvalidUserNicknameException("user nickname is too short");
        } else if(userNickname.length() > 15) {
            throw new InvalidUserNicknameException("user nickname is too long");
        } else {
            this.userNickname = userNickname;
        }
    }

    public void setUserName(String userName) {
        if(userName == null || userName.isBlank()) {
            throw new InvalidUsernameException("username is blank");
        } else if (userName.length() < 2) {
            throw new InvalidUsernameException("username is too short");
        } else if (userName.length() > 8) {
            throw new InvalidUsernameException("username is too long");
        } else {
            this.userName = userName;
        }
    }

    public void setUserGender(Integer userGender) {
        if(userGender == null) {
            throw new InvalidGenderException("user gender is blank");
        } else if( userGender < 0 || userGender > 2) {
            throw new InvalidGenderException("user gender is out of range");
        } else {
            this.userGender = userGender;
        }
    }

}
