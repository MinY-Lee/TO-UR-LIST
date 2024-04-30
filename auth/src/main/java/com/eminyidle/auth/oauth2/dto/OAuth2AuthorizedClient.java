package com.eminyidle.auth.oauth2.dto;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Table(name = "oauth2_authorized_client")
public class OAuth2AuthorizedClient implements Serializable {

    @EmbeddedId
    private GoogleRepoId id;

    private String accessTokenType;

    String accessTokenValue;

    private LocalDateTime accessTokenIssuedAt;

    private LocalDateTime accessTokenExpiresAt;

    private String accessTokenScopes;

    @Lob
    private byte[] refreshTokenValue;

    private LocalDateTime refreshTokenIssuedAt;

    private LocalDateTime createdAt;
}
