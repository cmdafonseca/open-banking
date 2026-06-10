package github.com.cmdafonseca.openbanking.client;

import github.com.cmdafonseca.openbanking.entity.Transaction;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface TransactionApiClient {
    public List<Transaction> getAccountTransactions(String accountNb);
}