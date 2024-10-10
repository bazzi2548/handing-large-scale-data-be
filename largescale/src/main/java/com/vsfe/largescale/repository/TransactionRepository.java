package com.vsfe.largescale.repository;

import com.vsfe.largescale.domain.Transaction;
import com.vsfe.largescale.model.PageInfo;
import com.vsfe.largescale.model.type.TransactionSearchOption;
import com.vsfe.largescale.util.C4PageTokenUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;

    /**
     * pageToken 을 사용하지 않고 Cursor 페이징 쿼리를 호출한다.
     *
     * @param accountNumber
     * @param option
     * @param size
     * @return
     */
    public PageInfo<Transaction> findTransactionWithoutPageToken(
            String accountNumber,
            TransactionSearchOption option,
            int size) {
        var data = switch (option) {
            case SENDER -> transactionJpaRepository.findTransactionsBySenderAccount(accountNumber, size + 1);
            case RECEIVER -> transactionJpaRepository.findTransactionsByReceiverAccount(accountNumber, size + 1);
            case ALL -> mergeAllOptions(
                    transactionJpaRepository.findTransactionsBySenderAccount(accountNumber, size + 1),
                    transactionJpaRepository.findTransactionsByReceiverAccount(accountNumber, size + 1),
                    size
            );
        };

        return PageInfo.of(data, size, Transaction::getTransactionDate, Transaction::getId);
        // sender 인 경우, receiver인 경우를 모두 가져옴
        // 그리고 두개를 합쳐서, transaction date 기준으로 내림차순 정렬하고, id 기준으로 오름차순 정렬하면
        // 가장 최근 거래 count 건이 나옴이 보장됨
        // "Modern JAVA User"
    }

    /**
     * pageToken 을 포함하여 Cursor 페이징 쿼리를 호출한다.
     *
     * @param accountNumber
     * @param pageToken
     * @param option
     * @param size
     * @return
     */
    public PageInfo<Transaction> findTransactionWithPageToken(
            String accountNumber,
            String pageToken,
            TransactionSearchOption option,
            int size
    ) {
        var pageData = C4PageTokenUtil.decodePageToken(pageToken, Instant.class, Integer.class);
        var transactionDate = pageData.getLeft();
        var transactionId = pageData.getRight();

        var data = switch (option) {
            case SENDER ->
                    transactionJpaRepository.findTransactionsBySenderAccountWithPageToken(accountNumber, transactionDate, transactionId, size + 1);
            case RECEIVER ->
                    transactionJpaRepository.findTransactionsByReceiverAccountWithPageToken(accountNumber, transactionDate, transactionId, size + 1);
            case ALL -> mergeAllOptions(
                    transactionJpaRepository.findTransactionsBySenderAccountWithPageToken(accountNumber, transactionDate, transactionId, size + 1),
                    transactionJpaRepository.findTransactionsByReceiverAccountWithPageToken(accountNumber, transactionDate, transactionId, size + 1),
                    size
            );
        };

        return PageInfo.of(data, size, Transaction::getTransactionDate, Transaction::getId);
        // pageToken 파싱해서, 날짜와 id를 가져옴
        // 그거 기반으로 위와 동일하게 쿼리 때림
        // 그리고 그 결과로 pageInfo 만들어주고 반환하면 됨
    }

    /**
     * 두 결과를 합쳐서, 데이터를 정렬 조건에 맞춰 count 개 만큼 가져온다.
     *
     * @param sendResult
     * @param receiverResult
     * @param size
     * @return
     */
    private List<Transaction> mergeAllOptions(
            List<Transaction> sendResult,
            List<Transaction> receiverResult,
            int size
    ) {
        return ListUtils.union(sendResult, receiverResult).stream()
                .sorted(
                        Comparator.comparing(Transaction::getTransactionDate).reversed()
                                .thenComparing(Transaction::getId)
                )
                .limit(size)
                .toList();
    }
}
