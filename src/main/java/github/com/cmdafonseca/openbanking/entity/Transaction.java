package github.com.cmdafonseca.openbanking.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction {
  @Id
  @GeneratedValue(strategy= GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID publicId;
  @Enumerated(EnumType.STRING)
  private Type type;
  private OffsetDateTime date;
  private String accountNumber;
  private String currency;
  private BigDecimal amount;
  @Builder.Default
  @Embedded
  private MerchantData merchantData = new MerchantData();

  public enum Type {
    DEPOSIT, WITHDRAWAL, PAYMENT, TRANSFER;
  }
}
