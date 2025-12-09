package com.bankingsystem.accountservice.service;

import com.bankingsystem.accountservice.model.Account;
import com.bankingsystem.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // create account
    public Account createAccount(Account account) {
        // starting balance null ayite 0 ga petta
        if (account.getBalance() == 0) {
            account.setBalance(0.0);
        }
        return accountRepository.save(account);
    }

    // all accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // single account by id
    public Account getAccountById(String id) {
        return accountRepository.findById(id).orElse(null);
    }

    // update balance (simple overwrite)
    public Account updateBalance(String id, double newBalance) {
        Account acc = getAccountById(id);
        if (acc == null) return null;
        acc.setBalance(newBalance);
        return accountRepository.save(acc);
    }
}