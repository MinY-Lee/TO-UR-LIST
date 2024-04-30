package com.eminyidle.user.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    private String userId;
    private String userGoogleId;
    private String userNickname;
    private String userName;
    private LocalDateTime userBirth;
    private Integer userGender;
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
