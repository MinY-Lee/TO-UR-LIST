package com.eminyidle.auth.oauth2.repository;


import com.eminyidle.auth.oauth2.dto.GoogleRepoId;
import com.eminyidle.auth.oauth2.dto.OAuth2AuthorizedClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleRepository extends JpaRepository<OAuth2AuthorizedClient, GoogleRepoId> {

    void delete(OAuth2AuthorizedClient entity);
}
