package finwiz.app.dto.bankaccount;

import finwiz.app.entity.bankaccount.AccountType;
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
