package finwiz.app.dto.transaction;

import finwiz.app.entity.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponse(
    Long id,
    String categoryName,
    String bankAccountName,
    TransactionType type,
    BigDecimal amount,
    String description,
    LocalDate transactionDate,
    LocalDateTime createdAt
) {}
