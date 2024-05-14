package com.eminyidle.user.adapter.out.persistence;

import com.eminyidle.user.adapter.dto.UserEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	Optional<UserEntity> findByUserNicknameAndDeletedAtIsNull(String userNickname);

	Optional<UserEntity> findByUserIdAndDeletedAtIsNull(String userId);

	@Query("SELECT u FROM UserEntity u WHERE u.userNickname LIKE %:userNickname% AND u.deletedAt IS NULL")
	List<UserEntity> findByUserNicknameContaining(String userNickname);
}
