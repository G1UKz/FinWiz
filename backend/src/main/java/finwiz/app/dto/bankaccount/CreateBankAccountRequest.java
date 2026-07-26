package finwiz.app.dto.bankaccount;

import finwiz.app.entity.bankaccount.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBankAccountRequest(
    @NotBlank String name,
    String bankName,
    @NotNull AccountType accountType,
    String currencyCode,
    Long initialBalanceMinor,
    Boolean isActive,
    String note
) {}
