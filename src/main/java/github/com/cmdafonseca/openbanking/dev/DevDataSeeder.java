package github.com.cmdafonseca.openbanking.dev;

import github.com.cmdafonseca.openbanking.entity.MerchantData;
import github.com.cmdafonseca.openbanking.entity.Transaction;
import github.com.cmdafonseca.openbanking.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@AllArgsConstructor
public class DevDataSeeder implements ApplicationRunner {

  private final TransactionRepository repo;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    repo.deleteAll();
    repo.saveAll(buildTransactions("37f37f92-f2d0-4e2d-92f0-17ff6589dec8"));
    repo.saveAll(buildTransactions(UUID.randomUUID().toString()));
  }

  private static List<Transaction> buildTransactions(String accountNumber) {
    List<Transaction> transactionList = new ArrayList<>();
    for (Transaction.Type type : Transaction.Type.values()) {
      transactionList.add(Transaction.builder()
          .accountNumber(accountNumber)
          .type(type)
          .date(OffsetDateTime.now())
          .currency("CHF")
          .amount(BigDecimal.valueOf(10))
          .merchantData(MerchantData.builder().merchantName("acme").build())
          .build());
    }
    return transactionList;
  }
}
