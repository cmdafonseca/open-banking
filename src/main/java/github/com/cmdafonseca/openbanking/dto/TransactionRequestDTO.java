package github.com.cmdafonseca.openbanking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDTO {
  private UUID id;
  private String type;
  private LocalDateTime date;
  private UUID accountNumber;
  private String currency;
  private BigDecimal amount;
  private String merchantName;
  private String merchantLogo;
}
