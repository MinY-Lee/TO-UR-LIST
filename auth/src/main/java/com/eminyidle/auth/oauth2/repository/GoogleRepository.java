package com.eminyidle.auth.oauth2.repository;


import com.eminyidle.auth.oauth2.dto.GoogleRepoId;
import com.eminyidle.auth.oauth2.dto.OAuth2AuthorizedClientEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleRepository extends JpaRepository<OAuth2AuthorizedClientEntity, GoogleRepoId> {
    void delete(OAuth2AuthorizedClientEntity entity);

    Optional<OAuth2AuthorizedClientEntity> findBy
}
