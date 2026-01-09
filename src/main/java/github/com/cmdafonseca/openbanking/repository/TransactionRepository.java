package github.com.cmdafonseca.openbanking.repository;

import github.com.cmdafonseca.openbanking.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
  List<Transaction> findByAccountNumber(String accountNumber);
}
