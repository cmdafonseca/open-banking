package github.com.cmdafonseca.openbanking.service;

import github.com.cmdafonseca.openbanking.client.TransactionApiClient;
import github.com.cmdafonseca.openbanking.dto.TransactionResponseDTO;
import github.com.cmdafonseca.openbanking.entity.MerchantData;
import github.com.cmdafonseca.openbanking.entity.Transaction;
import github.com.cmdafonseca.openbanking.mappers.TransactionMapper;
import github.com.cmdafonseca.openbanking.merchant.MerchantDetailsRepository;
import github.com.cmdafonseca.openbanking.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

  @Autowired
  @Lazy
  private TransactionService self;

  private final TransactionRepository transactionRepository;

  private final TransactionApiClient apiClient;
  private final MerchantDetailsRepository merchantRepo;

  public List<TransactionResponseDTO> findAllByAccountNumber(String accountNumber) {
    validateAccountNumber(accountNumber);  // ← validate BEFORE circuit breaker
    return self.fetchAndEnrich(accountNumber);
  }

  @CircuitBreaker(name = "transactionService", fallbackMethod = "getTransactionsFallback")
  public List<TransactionResponseDTO> fetchAndEnrich(String accountNumber) {
    List<Transaction> transactionsList = apiClient.getAccountTransactions(accountNumber);

    if (!transactionsList.isEmpty()) {
      return transactionsList.stream().map(transaction -> {
            Optional.ofNullable(transaction.getMerchantData())
                .map(MerchantData::getMerchantName)
                .map(merchantRepo::findLogoByMerchantName)
                .ifPresent(logo -> transaction.getMerchantData().setMerchantLogo(logo));
            return transaction;
          })
          .map(TransactionMapper::toDTO)
          .toList();
    } else {
      return Collections.emptyList();
    }
  }

  private void validateAccountNumber(String accountNumber) {
    if (accountNumber.isEmpty() || accountNumber.isBlank()) {
      throw new IllegalArgumentException("Account number can not be empty");
    }
    try {
      UUID.fromString(accountNumber);
    } catch (
        IllegalArgumentException e) {
      throw new IllegalArgumentException(accountNumber + " is not a correct UUID");
    }
  }

  // Fallback — called when circuit breaker opens
  public List<TransactionResponseDTO> getTransactionsFallback(String accountNumber, Exception e) {
    return transactionRepository
        .findByAccountNumber(accountNumber)
        .stream()
        .map(transaction -> {
          Optional.ofNullable(transaction.getMerchantData())
              .map(MerchantData::getMerchantName)
              .map(merchantRepo::findLogoByMerchantName)
              .ifPresent(logo -> transaction.getMerchantData().setMerchantLogo(logo));
          return transaction;
        })
        .map(TransactionMapper::toDTO)
        .toList();
  }
}
