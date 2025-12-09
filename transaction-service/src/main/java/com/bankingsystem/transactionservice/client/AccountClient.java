package com.bankingsystem.transactionservice.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("/api/accounts/{id}")
    AccountDto getAccount(@PathVariable("id") String id);

    @PutMapping("/api/accounts/{id}/balance")
    AccountDto updateBalance(
            @PathVariable("id") String id,
            @RequestParam("amount") double amount
    );

    @Data
    class AccountDto {
        private String id;
        private String name;
        private String email;
        private double balance;
    }
}