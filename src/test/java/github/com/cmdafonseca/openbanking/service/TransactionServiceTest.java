package github.com.cmdafonseca.openbanking.service;

import github.com.cmdafonseca.openbanking.entity.MerchantData;
import github.com.cmdafonseca.openbanking.entity.Transaction;
import github.com.cmdafonseca.openbanking.repository.TransactionRepository;
import github.com.cmdafonseca.openbanking.utils.TestUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  TransactionService transactionService;

  @BeforeEach
  void setUp() {
    transactionRepository.deleteAll();
    transactionRepository.saveAll(TestUtils.buildTransactions("37f37f92-f2d0-4e2d-92f0-17ff6589dec8"));
    transactionRepository.saveAll(TestUtils.buildTransactions(UUID.randomUUID().toString()));
  }

  private static final String ACCOUNT_NB = "37f37f92-f2d0-4e2d-92f0-17ff6589dec8";

  @Test
  void findAllByAccountNumber_shouldReturnThreeTransactions() {
    int result = transactionService.findAllByAccountNumber(ACCOUNT_NB).size();
    assertEquals(4, result, "Expected 4 transactions, but got " + result);
  }

}
