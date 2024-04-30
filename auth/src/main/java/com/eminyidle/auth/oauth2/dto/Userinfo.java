package com.eminyidle.auth.oauth2.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Userinfo {

    @Id
    private String userGoogleId;

    @Column(name = "userId")
    private String userId = UUID.randomUUID().toString();

}
