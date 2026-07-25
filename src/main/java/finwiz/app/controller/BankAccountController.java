package finwiz.app.controller;

import finwiz.app.dto.BankAccountResponse;
import finwiz.app.dto.CreateBankAccountRequest;
import finwiz.app.service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/users/{userId}/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccountResponse createAccount(
            @PathVariable Long userId,
            @Valid @RequestBody CreateBankAccountRequest request) {
        return bankAccountService.createAccount(userId, request);
    }

    @GetMapping("/users/{userId}/accounts")
    public List<BankAccountResponse> listAccounts(@PathVariable Long userId) {
        return bankAccountService.listAccountsByUser(userId);
    }

    @GetMapping("/accounts/{id}")
    public BankAccountResponse getAccount(@PathVariable Long id) {
        return bankAccountService.getAccount(id);
    }
}
