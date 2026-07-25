package finwiz.app.service;

import finwiz.app.dto.BankAccountResponse;
import finwiz.app.dto.CreateBankAccountRequest;
import finwiz.app.entity.BankAccount;
import finwiz.app.entity.Currency;
import finwiz.app.entity.User;
import finwiz.app.exception.ResourceNotFoundException;
import finwiz.app.repository.BankAccountRepository;
import finwiz.app.repository.CurrencyRepository;
import finwiz.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    @Transactional
    public BankAccountResponse createAccount(Long userId, CreateBankAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String currencyCode = request.currencyCode() != null ? request.currencyCode() : "RUB";
        Currency currency = currencyRepository.findById(currencyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found: " + currencyCode));

        BankAccount account = BankAccount.builder()
                .user(user)
                .name(request.name())
                .bankName(request.bankName())
                .accountType(request.accountType())
                .currency(currency)
                .balanceMinor(request.initialBalanceMinor() != null ? request.initialBalanceMinor() : 0L)
                .isActive(request.isActive() != null ? request.isActive() : true)
                .note(request.note())
                .build();

        return toResponse(bankAccountRepository.save(account));
    }

    @Transactional(readOnly = true)
    public BankAccountResponse getAccount(Long id) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));
        return toResponse(account);
    }

    @Transactional(readOnly = true)
    public List<BankAccountResponse> listAccountsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return bankAccountRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private BankAccountResponse toResponse(BankAccount account) {
        return new BankAccountResponse(
                account.getId(),
                account.getName(),
                account.getBankName(),
                account.getAccountType(),
                account.getCurrency() != null ? account.getCurrency().getCode() : null,
                account.getBalance(),
                account.getIsActive(),
                account.getNote(),
                account.getCreatedAt()
        );
    }
}
