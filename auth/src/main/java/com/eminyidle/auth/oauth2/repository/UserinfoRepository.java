package com.eminyidle.auth.oauth2.repository;

import com.eminyidle.auth.oauth2.dto.Userinfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserinfoRepository extends JpaRepository<Userinfo, String> {
    Optional<Userinfo> findByUserId(String userId);
}
