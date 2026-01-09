package github.com.cmdafonseca.openbanking.service;

import github.com.cmdafonseca.openbanking.dto.TransactionResponseDTO;
import github.com.cmdafonseca.openbanking.mappers.TransactionMapper;
import github.com.cmdafonseca.openbanking.entity.MerchantData;
import github.com.cmdafonseca.openbanking.repository.TransactionRepository;
import java.util.List;
import github.com.cmdafonseca.openbanking.entity.Transaction;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  @Autowired
  TransactionRepository transactionRepository;

  public List<TransactionResponseDTO> findAllByAccountNumber(String accountNumber) {
    if (accountNumber.isEmpty() || accountNumber.isBlank()) {
     throw new IllegalArgumentException("Account number can not be empty");
    }

    try {
      UUID.fromString(accountNumber);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException( accountNumber + " is not a correct UUID" );
    }

    List<Transaction> transactionsList = transactionRepository.findByAccountNumber(accountNumber);

    if (!transactionsList.isEmpty()) {
      return transactionsList.stream().map(TransactionMapper::toDTO).toList();
  }
    else throw new IllegalArgumentException("Account number not found: " + accountNumber);
  }

}
