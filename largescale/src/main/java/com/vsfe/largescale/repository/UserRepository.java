package com.vsfe.largescale.repository;

import com.vsfe.largescale.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserJpaRepository userJpaRepository;

    /**
     * 최근에 가입한 유저의 목록을 가져온다.
     * @param count
     * @return
     */
    public List<User> findRecentCreatedUsers(int count) {
        return userJpaRepository.findRecentCreatedUsers(count);
    }
}
