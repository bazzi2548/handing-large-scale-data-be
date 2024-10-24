package com.vsfe.largescale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vsfe.largescale.domain.Account;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Long> {

	// 페이징 쿼리
	@Query("""
		SELECT a
		FROM Account a
		ORDER BY a.id
		LIMIT :size
		""")
	List<Account> findAccount(
		@Param("size") int size
	);

	// index: pk
	@Query("""
		SELECT a
		FROM Account a
		WHERE a.id > :lastAccountId
		ORDER BY a.id
		LIMIT :size
		""")
	List<Account> findAccountWithLastAccountId(
		@Param("lastAccountId") int lastAccountId,
		@Param("size") int size
	);

	@Query("""
		SELECT a
		FROM Account a
		WHERE a.userId = :userId
		ORDER BY a.id
		LIMIT :size
		""")
	List<Account> findAccountByUserId(
		@Param("userId") int userId,
		@Param("size") int size
	);

	@Query("""
		SELECT a
		FROM Account a
		WHERE a.userId = :userId AND a.id > :lastAccountId
		ORDER BY a.id
		LIMIT :size
		""")
	List<Account> findAccountByUserIdWithLastUserId(
		@Param("userId") int userId,
		@Param("lastAccountId") int lastAccountId,
		@Param("size") int size
	);
}
