package finwiz.app.dto;

import finwiz.app.entity.enums.AccountType;
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
