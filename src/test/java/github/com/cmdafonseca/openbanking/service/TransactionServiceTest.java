package github.com.cmdafonseca.openbanking.service;

import github.com.cmdafonseca.openbanking.client.TransactionApiClient;
import github.com.cmdafonseca.openbanking.merchant.MerchantDetailsRepository;
import github.com.cmdafonseca.openbanking.repository.TransactionRepository;
import github.com.cmdafonseca.openbanking.utils.TestUtils;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServiceTest {

  @MockitoBean
  TransactionApiClient transactionApiClient;

  @MockitoBean
  MerchantDetailsRepository merchantRepo;

  @Autowired
  TransactionService transactionService;

  private static final String ACCOUNT_NB = "37f37f92-f2d0-4e2d-92f0-17ff6589dec8";

  @BeforeEach
  void setUp() {
    when(transactionApiClient.getAccountTransactions(ACCOUNT_NB))
        .thenReturn(TestUtils.buildTransactions(ACCOUNT_NB));
    when(merchantRepo.findLogoByMerchantName(any()))
        .thenReturn("logo.png");
  }

  @Test
  void findAllByAccountNumber_shouldReturnThreeTransactions() {
    int result = transactionService.findAllByAccountNumber(ACCOUNT_NB).size();
    assertEquals(4, result, "Expected 4 transactions, but got " + result);
  }

}
