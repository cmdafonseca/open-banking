package github.com.cmdafonseca.openbanking.web;

import github.com.cmdafonseca.openbanking.client.TransactionApiClient;
import github.com.cmdafonseca.openbanking.merchant.MerchantDetailsRepository;
import github.com.cmdafonseca.openbanking.utils.TestUtils;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static io.restassured.RestAssured.given;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isIn;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIntegrationTest {

  @MockitoBean
  TransactionApiClient transactionApiClient;

  @MockitoBean
  MerchantDetailsRepository merchantDetailsRepository;

  @LocalServerPort
  private int port;

  private static final String ACCOUNT_NB = "37f37f92-f2d0-4e2d-92f0-17ff6589dec8";

  @BeforeEach
  void setUp() {
    when(transactionApiClient.getAccountTransactions(ACCOUNT_NB))
        .thenReturn(TestUtils.buildTransactions(ACCOUNT_NB));
    when(merchantDetailsRepository.findLogoByMerchantName(any()))
        .thenReturn("logo.png");
  }

  @Test
  void checkNoAccountNumberReturnsNoData() throws Exception {
    given()
        .port(port)
    .when()
        .get("/api/v1/transactions/")
        .then()
        .statusCode(404)
        .body("error", equalTo("Not Found"));
  }

  @Test
  void checkInvalidUUIDFormatReturnsError() {
    String accountNumber = "invalid-uuid-format";
    given()
        .port(port)
        .pathParam("accountNumber", accountNumber)
        .when()
        .get("/api/v1/transactions/{accountNumber}")
        .then()
        .log().body()
        .statusCode(404)
        .body("endUserMessage", equalTo("invalid-uuid-format is not a correct UUID"));
  }

  @Test
  void returnsFourTransactions() {
    given()
        .port(port)
        .pathParam("accountNumber", ACCOUNT_NB)
    .when()
        .get("/api/v1/transactions/{accountNumber}")
        .then()
        .log().body()
        .statusCode(200)
        .body("size()", equalTo(4));
  }

  @Test
  void checkValidAccountReturnsCorrectData() {
    given()
        .port(port)
        .pathParam("accountNumber", ACCOUNT_NB)
        .when()
        .get("/api/v1/transactions/{accountNumber}")
        .then()
        .log().body()
        .statusCode(200)
        .body("size()", equalTo(4))
        .body("[0].accountId", equalTo("37f37f92-f2d0-4e2d-92f0-17ff6589dec8"))
        .body("[0].currency", equalTo("CHF"))
        .body("[0].amount", equalTo(10))
        .body("[0].type", isIn(List.of("TRANSFER", "DEPOSIT", "WITHDRAWAL", "PAYMENT")));
  }

  @Test
  void checkSchemaValidation() {
    given()
        .port(port)
        .pathParam("accountNumber", ACCOUNT_NB)
        .when()
        .get("/api/v1/transactions/{accountNumber}")
        .then()
        .assertThat()
        .body(matchesJsonSchemaInClasspath("utils/transaction-dto-schema.json"));
  }
}
