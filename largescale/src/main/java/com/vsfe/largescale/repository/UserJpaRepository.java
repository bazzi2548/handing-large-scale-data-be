package com.vsfe.largescale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vsfe.largescale.domain.User;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

	// index: user_idx01 (create_date, user_id)
	@Query("""
		SELECT u
		FROM User u
		ORDER BY u.createDate DESC, u.id ASC
		LIMIT :count
		""")
	List<User> findRecentCreatedUsers(@Param("count") int count);

	// index: pk
	@Query("""
		SELECT u
		FROM User u
		WHERE u.id > :lastUserId
		ORDER BY u.id
		LIMIT :count
		""")
	List<User> findUsersWithLastUserId(
		@PositiveOrZero @Param("lastUserId") int lastUserId,
		@Positive @Param("count") int count
	);
}
