package com.vsfe.largescale.repository;

import com.vsfe.largescale.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
