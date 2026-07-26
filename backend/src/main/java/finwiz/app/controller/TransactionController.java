package finwiz.app.controller;

import finwiz.app.dto.CreateTransactionRequest;
import finwiz.app.dto.TransactionResponse;
import finwiz.app.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/users/{userId}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @PathVariable Long userId,
            @Valid @RequestBody CreateTransactionRequest request) {
        return transactionService.createTransaction(userId, request);
    }

    @GetMapping("/users/{userId}/transactions")
    public List<TransactionResponse> listUserTransactions(@PathVariable Long userId) {
        return transactionService.listByUser(userId);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<TransactionResponse> listAccountTransactions(@PathVariable Long accountId) {
        return transactionService.listByAccount(accountId);
    }

    @GetMapping("/transactions/{id}")
    public TransactionResponse getTransaction(@PathVariable Long id) {
        return transactionService.getTransaction(id);
    }
}
