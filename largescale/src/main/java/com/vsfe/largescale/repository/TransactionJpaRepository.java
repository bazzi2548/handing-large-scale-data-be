package com.vsfe.largescale.repository;

import com.vsfe.largescale.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.senderAccount = :account
            order by t.transactionDate DESC, t.id ASC
            limit :limit
            """)
    List<Transaction> findTransactionsBySenderAccount(
            @Param("account") String account,
            @Param("limit") int limit
    );

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.receiverAccount = :account
            order by t.transactionDate DESC, t.id ASC
            limit :limit
            """)
    List<Transaction> findTransactionsByReceiverAccount(
            @Param("account") String account,
            @Param("limit") int limit
    );

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.senderAccount = :account
            AND ((t.transactionDate < :transactionDate) OR (t.transactionDate = :transactionDate AND t.id > :id))
            order by t.transactionDate DESC, t.id ASC
            limit :limit
            """
    )
    List<Transaction> findTransactionsBySenderAccountWithPageToken(
            @Param("account") String account,
            @Param("transactionDate") Instant transactionDate,
            @Param("id") int id,
            @Param("limit") int limit
    );

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.receiverAccount = :account
            AND ((t.transactionDate < :transactionDate) OR (t.transactionDate = :transactionDate AND t.id > :id))
            order by t.transactionDate DESC, t.id ASC
            limit :limit
            """
    )
    List<Transaction> findTransactionsByReceiverAccountWithPageToken(
            @Param("account") String account,
            @Param("transactionDate") Instant transactionDate,
            @Param("id") int id,
            @Param("limit") int limit
    );

}
