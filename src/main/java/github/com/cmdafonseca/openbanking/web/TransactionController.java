package github.com.cmdafonseca.openbanking.web;

import github.com.cmdafonseca.openbanking.dto.TransactionResponseDTO;
import github.com.cmdafonseca.openbanking.entity.ErrorResponse;
import github.com.cmdafonseca.openbanking.service.TransactionService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
  private final TransactionService transactionService;

  @GetMapping("/{accountNumber}")
  public List<TransactionResponseDTO> getTransactions(@PathVariable final String accountNumber) {
      return transactionService.findAllByAccountNumber(accountNumber);
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ErrorResponse handleError(HttpServletRequest req, IllegalArgumentException ex) {
    return new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.toString(),
        java.util.Objects.requireNonNullElse(ex.getMessage(), HttpStatus.NOT_FOUND.toString()),
        req.getRequestURI()
    );
  }

}
