package finwiz.app.dto.transaction;

import finwiz.app.entity.transaction.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CreateTransactionRequest(
    @NotNull Long bankAccountId,
    @NotNull Long categoryId,
    @NotNull TransactionType type,
    @NotNull @Positive Long amountMinor,
    String description,
    @NotNull LocalDate transactionDate
) {}
