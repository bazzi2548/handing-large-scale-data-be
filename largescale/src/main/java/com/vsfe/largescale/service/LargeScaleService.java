package com.vsfe.largescale.service;

import com.vsfe.largescale.domain.User;
import com.vsfe.largescale.repository.AccountJpaRepository;
import com.vsfe.largescale.repository.TransactionJpaRepository;
import com.vsfe.largescale.repository.UserRepository;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LargeScaleService {
    private final AccountJpaRepository accountJpaRepository;
    private final TransactionJpaRepository transactionJpaRepository;
    private final UserRepository userRepository;

    /**
     * 최신 유저의 목록을 가져올 것이다. (count 만큼)
     * "최신"의 조건을 바꿔가면서 쿼리를 날려보자.
     * @param count
     * @return
     */
    public List<User> getUserInfo(@Max(100) int count) {

        return userRepository.findRecentCreatedUsers(count);
    }

    public void getTransactions() {

    }

    public void validateAccountNumber() {

    }

    public void aggregateTransactions() {

    }

    public void aggregateTransactionsWithSharding() {

    }
}
