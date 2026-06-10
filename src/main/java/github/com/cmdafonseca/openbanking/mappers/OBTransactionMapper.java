package github.com.cmdafonseca.openbanking.mappers;

import github.com.cmdafonseca.model.OBCreditDebitCode1;
import github.com.cmdafonseca.model.OBTransaction6;
import github.com.cmdafonseca.openbanking.entity.MerchantData;
import github.com.cmdafonseca.openbanking.entity.Transaction;
import java.math.BigDecimal;

public class OBTransactionMapper {

  private OBTransactionMapper() {}

  public static Transaction toTransaction(OBTransaction6 obTransaction6) {
    var builder = Transaction.builder();

    var obAmount = obTransaction6.getAmount();

    if (obAmount == null || obAmount.getAmount() == null ) {
      throw new IllegalArgumentException("Transaction missing required amount");
    }

    BigDecimal amount = new BigDecimal(obAmount.getAmount());

    var currencyExchange = obTransaction6.getCurrencyExchange();

    if (currencyExchange != null) {
      builder.amount(amount.multiply(currencyExchange.getExchangeRate()));
      if (currencyExchange.getUnitCurrency() != null) {
        builder.currency(currencyExchange.getUnitCurrency() );
      }
    } else {
      builder.amount(amount);
    }

    if (obTransaction6.getAccountId() != null) {
      builder.accountNumber(obTransaction6.getAccountId());
    }

    if (obTransaction6.getCreditDebitIndicator() != null) {
      builder.type(typeMapper(obTransaction6.getCreditDebitIndicator()));
    }

    if (obTransaction6.getMerchantDetails() != null) {
      if (obTransaction6.getMerchantDetails().getMerchantName() != null) {
        builder.merchantData(MerchantData.builder().merchantName(obTransaction6.getMerchantDetails().getMerchantName()).build());
      }
    }

    if (obTransaction6.getValueDateTime() != null) {
      builder.date(obTransaction6.getValueDateTime());
    }
    return builder.build();
  }

  private static Transaction.Type typeMapper(OBCreditDebitCode1 oBCreditDebitCode1) {
    return switch (oBCreditDebitCode1) {
      case CREDIT -> Transaction.Type.DEPOSIT;
      case DEBIT -> Transaction.Type.WITHDRAWAL;
    };
  }

}
