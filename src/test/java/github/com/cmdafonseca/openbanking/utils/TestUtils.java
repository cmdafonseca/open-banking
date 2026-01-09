package github.com.cmdafonseca.openbanking.utils;

import github.com.cmdafonseca.openbanking.entity.MerchantData;
import github.com.cmdafonseca.openbanking.entity.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

  public static List<Transaction> buildTransactions(String accountNumber) {
    List<Transaction> transactionList = new ArrayList<Transaction>();
    for (Transaction.Type type : Transaction.Type.values()) {
      transactionList.add(Transaction.builder()
          .accountNumber(accountNumber)
          .type(type)
          .date(LocalDateTime.now())
          .currency("CHF")
          .amount(BigDecimal.valueOf(10))
          .merchantData(MerchantData.builder().build())
          .build());
    }
    return transactionList;
  }
}
