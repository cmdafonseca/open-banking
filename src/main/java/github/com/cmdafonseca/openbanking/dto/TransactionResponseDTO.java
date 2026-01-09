package github.com.cmdafonseca.openbanking.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {
  private String accountId;
  private UUID userTransactionId;
  private BigDecimal amount;
  private String currency;
  private String description;
  private String type;
  private OffsetDateTime date;
}
