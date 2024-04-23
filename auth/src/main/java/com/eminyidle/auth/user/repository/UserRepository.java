package com.eminyidle.auth.user.repository;

import com.eminyidle.auth.user.dto.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserEmailAndDeletedAtIsNull(String userEmail);

    Optional<User> findByUserIdAndDeletedAtIsNull(String userId);

    Optional<User> findByUserId(String userId);

    Optional<User> findByUserGoogleId(String userGoogleId);

    Optional<User> findByUserEmail(String userEmail);
}
