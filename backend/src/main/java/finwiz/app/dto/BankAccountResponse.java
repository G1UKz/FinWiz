package finwiz.app.dto;

import finwiz.app.entity.enums.AccountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankAccountResponse(
    Long id,
    String name,
    String bankName,
    AccountType accountType,
    String currencyCode,
    BigDecimal balance,
    Boolean isActive,
    String note,
    LocalDateTime createdAt
) {}
