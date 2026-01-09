package github.com.cmdafonseca.openbanking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  private int code;
  private String error;
  private String endUserMessage;
  private String path;
}

