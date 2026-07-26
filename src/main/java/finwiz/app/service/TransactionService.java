package finwiz.app.service;

import finwiz.app.dto.CreateTransactionRequest;
import finwiz.app.dto.TransactionResponse;
import finwiz.app.entity.BankAccount;
import finwiz.app.entity.Category;
import finwiz.app.entity.Transaction;
import finwiz.app.entity.User;
import finwiz.app.exception.ResourceNotFoundException;
import finwiz.app.repository.BankAccountRepository;
import finwiz.app.repository.CategoryRepository;
import finwiz.app.repository.TransactionRepository;
import finwiz.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TransactionResponse createTransaction(Long userId, CreateTransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BankAccount account = bankAccountRepository.findById(request.bankAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Transaction transaction = Transaction.builder()
                .user(user)
                .bankAccount(account)
                .category(category)
                .type(request.type())
                .amountMinor(request.amountMinor())
                .description(request.description())
                .transactionDate(request.transactionDate())
                .build();

        return toResponse(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return toResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> listByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return transactionRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> listByAccount(Long accountId) {
        if (!bankAccountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Bank account not found");
        }
        return transactionRepository.findByBankAccountId(accountId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getCategory() != null ? transaction.getCategory().getName() : null,
                transaction.getBankAccount() != null ? transaction.getBankAccount().getName() : null,
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.getCreatedAt()
        );
    }
}
