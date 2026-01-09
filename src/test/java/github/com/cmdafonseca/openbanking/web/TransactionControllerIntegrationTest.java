package github.com.cmdafonseca.openbanking.web;

import github.com.cmdafonseca.openbanking.repository.TransactionRepository;
import github.com.cmdafonseca.openbanking.utils.TestUtils;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isIn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIntegrationTest {

  @Autowired
  TransactionRepository transactionRepository;

  private static final String ACCOUNT_NB = "37f37f92-f2d0-4e2d-92f0-17ff6589dec8";

  @BeforeEach
  void setUp() {
    transactionRepository.deleteAll();
    transactionRepository.saveAll(TestUtils.buildTransactions("37f37f92-f2d0-4e2d-92f0-17ff6589dec8"));
    transactionRepository.saveAll(TestUtils.buildTransactions(UUID.randomUUID().toString()));
  }

  @LocalServerPort
  private int port;

  @Test
  void checkMissingAccountNumberReturnsNotFound() {
    // This test will fail now as we have hardcoded data
    String accountNumber = "5607b413-7405-41de-94cf-68224779ae84";
    given()
        .port(port)
        .contentType("application/json")
        .pathParam("accountNumber", accountNumber)
        .when()
            .get("/api/v1/transactions/{accountNumber}")
        .then()
            .statusCode(404)
        .body("endUserMessage", equalTo("Account number not found: " + accountNumber));
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
        .body("[0].amount", equalTo(10f))
        .body("[0].type", isIn(List.of("TRANSFER", "DEPOSIT", "WITHDRAWAL", "PAYMENT")));
  }

  @Test
  void checkSchemaValidation() {;
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
