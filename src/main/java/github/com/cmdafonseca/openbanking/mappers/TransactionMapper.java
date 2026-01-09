package github.com.cmdafonseca.openbanking.mappers;

import github.com.cmdafonseca.openbanking.dto.TransactionResponseDTO;
import github.com.cmdafonseca.openbanking.entity.Transaction;

public class TransactionMapper {

  private TransactionMapper() {}

  public static TransactionResponseDTO toDTO(Transaction transaction) {

    var builder = TransactionResponseDTO.builder();

    if(transaction.getPublicId() != null) {
      builder.userTransactionId(transaction.getPublicId());
    }

    if(transaction.getAccountNumber() != null) {
      builder.accountId(transaction.getAccountNumber());
    }

    if (transaction.getAmount() != null) {
      builder.amount(transaction.getAmount());
    }

    if (transaction.getCurrency() != null) {
      builder.currency(transaction.getCurrency());
    }

    if(transaction.getType() != null) {
      builder.type(transaction.getType().name());
    }

    if(transaction.getDate() != null) {
      builder.date(transaction.getDate());
    }

    if (transaction.getMerchantData() == null) {
      builder.description("UNKNOWN");
    } else {
      var merchantName = transaction.getMerchantData().getMerchantName();
      var merchantLogo = transaction.getMerchantData().getMerchantLogo();
      if (merchantName == null) {
        builder.description("UNKNOWN");
      } else if (merchantLogo != null) {
        builder.description(merchantName + ", " + merchantLogo);
      }
    }
      return builder.build();
  }

}
