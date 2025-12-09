package com.bankingsystem.accountservice.controller;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // POST /api/accounts
    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account) {
        Account saved = accountService.createAccount(account);
        return ResponseEntity.ok(saved);
    }

    // GET /api/accounts
    @GetMapping
    public ResponseEntity<List<Account>> getAll() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // GET /api/accounts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable String id) {
        Account acc = accountService.getAccountById(id);
        if (acc == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(acc);
    }

    // PUT /api/accounts/{id}/balance?amount=123
    @PutMapping("/{id}/balance")
    public ResponseEntity<Account> updateBalance(
            @PathVariable String id,
            @RequestParam double amount) {

        Account updated = accountService.updateBalance(id, amount);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}