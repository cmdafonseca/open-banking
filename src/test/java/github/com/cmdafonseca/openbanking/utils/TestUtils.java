package github.com.cmdafonseca.openbanking.utils;

import github.com.cmdafonseca.openbanking.entity.MerchantData;
import github.com.cmdafonseca.openbanking.entity.Transaction;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {

  public static List<Transaction> buildTransactions(String accountNumber) {
    List<Transaction> transactionList = new ArrayList<Transaction>();
    for (Transaction.Type type : Transaction.Type.values()) {
      transactionList.add(Transaction.builder()
          .publicId(UUID.randomUUID())
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
