package finwiz.app.repository;

import finwiz.app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByBankAccountId(Long bankAccountId);
    List<Transaction> findByUserIdAndBankAccountId(Long userId, Long bankAccountId);
}
