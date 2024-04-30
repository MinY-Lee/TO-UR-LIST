package com.eminyidle.auth.oauth2.dto;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GoogleRepoId implements Serializable {
    private String clientRegistrationId;
    private String principalName;
}