package com.bankingsystem.transactionservice.controller;

import com.bankingsystem.transactionservice.model.Transaction;
import com.bankingsystem.transactionservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // POST /api/transactions/deposit?accountId=...&amount=...
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @RequestParam String accountId,
            @RequestParam double amount) {
        return ResponseEntity.ok(service.deposit(accountId, amount));
    }

    // POST /api/transactions/withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @RequestParam String accountId,
            @RequestParam double amount) {
        return ResponseEntity.ok(service.withdraw(accountId, amount));
    }

    // POST /api/transactions/transfer
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @RequestParam String fromAccountId,
            @RequestParam String toAccountId,
            @RequestParam double amount) {
        return ResponseEntity.ok(service.transfer(fromAccountId, toAccountId, amount));
    }

    // GET /api/transactions/account/{accountId}
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getByAccount(
            @PathVariable String accountId) {
        return ResponseEntity.ok(service.getByAccount(accountId));
    }
}