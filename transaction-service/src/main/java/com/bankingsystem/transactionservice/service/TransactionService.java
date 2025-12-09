package com.bankingsystem.transactionservice.service;

import com.bankingsystem.transactionservice.client.AccountClient;
import com.bankingsystem.transactionservice.client.NotificationClient;
import com.bankingsystem.transactionservice.model.Transaction;
import com.bankingsystem.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final NotificationClient notificationClient;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountClient accountClient,
                              NotificationClient notificationClient) {
        this.transactionRepository = transactionRepository;
        this.accountClient = accountClient;
        this.notificationClient = notificationClient;
    }

    // generate readable transactionId
    private String generateTxnId() {
        return "TXN-" + Instant.now().toEpochMilli() + "-" +
                UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    public Transaction deposit(String accountId, double amount) {
        var account = accountClient.getAccount(accountId);
        double newBalance = account.getBalance() + amount;
        accountClient.updateBalance(accountId, newBalance);

        Transaction txn = new Transaction();
        txn.setTransactionId(generateTxnId());
        txn.setType("DEPOSIT");
        txn.setAmount(amount);
        txn.setTimestamp(Instant.now());
        txn.setStatus("SUCCESS");
        txn.setDestinationAccount(accountId);

        Transaction saved = transactionRepository.save(txn);

        sendNotification(account.getEmail(),
                "Deposit successful",
                "Amount " + amount + " credited. New balance: " + newBalance);

        return saved;
    }

    public Transaction withdraw(String accountId, double amount) {
        var account = accountClient.getAccount(accountId);
        if (account.getBalance() < amount) {
            // not enough balance
            Transaction txn = new Transaction();
            txn.setTransactionId(generateTxnId());
            txn.setType("WITHDRAW");
            txn.setAmount(amount);
            txn.setTimestamp(Instant.now());
            txn.setStatus("FAILED");
            txn.setSourceAccount(accountId);
            return transactionRepository.save(txn);
        }

        double newBalance = account.getBalance() - amount;
        accountClient.updateBalance(accountId, newBalance);

        Transaction txn = new Transaction();
        txn.setTransactionId(generateTxnId());
        txn.setType("WITHDRAW");
        txn.setAmount(amount);
        txn.setTimestamp(Instant.now());
        txn.setStatus("SUCCESS");
        txn.setSourceAccount(accountId);

        Transaction saved = transactionRepository.save(txn);

        sendNotification(account.getEmail(),
                "Withdraw successful",
                "Amount " + amount + " debited. New balance: " + newBalance);

        return saved;
    }

    public Transaction transfer(String fromId, String toId, double amount) {
        // withdraw from source
        var source = accountClient.getAccount(fromId);
        var dest = accountClient.getAccount(toId);

        if (source.getBalance() < amount) {
            Transaction txn = new Transaction();
            txn.setTransactionId(generateTxnId());
            txn.setType("TRANSFER");
            txn.setAmount(amount);
            txn.setTimestamp(Instant.now());
            txn.setStatus("FAILED");
            txn.setSourceAccount(fromId);
            txn.setDestinationAccount(toId);
            return transactionRepository.save(txn);
        }

        double newSourceBal = source.getBalance() - amount;
        double newDestBal = dest.getBalance() + amount;

        accountClient.updateBalance(fromId, newSourceBal);
        accountClient.updateBalance(toId, newDestBal);

        Transaction txn = new Transaction();
        txn.setTransactionId(generateTxnId());
        txn.setType("TRANSFER");
        txn.setAmount(amount);
        txn.setTimestamp(Instant.now());
        txn.setStatus("SUCCESS");
        txn.setSourceAccount(fromId);
        txn.setDestinationAccount(toId);

        Transaction saved = transactionRepository.save(txn);

        sendNotification(source.getEmail(),
                "Amount transferred",
                "You sent " + amount + " to " + toId +
                        ". New balance: " + newSourceBal);
        sendNotification(dest.getEmail(),
                "Amount received",
                "You received " + amount + " from " + fromId +
                        ". New balance: " + newDestBal);

        return saved;
    }

    public List<Transaction> getByAccount(String accountId) {
        return transactionRepository.findBySourceAccountOrDestinationAccount(accountId, accountId);
    }

    private void sendNotification(String to, String subject, String message) {
        NotificationClient.NotificationRequest req = new NotificationClient.NotificationRequest();
        req.setTo(to);
        req.setSubject(subject);
        req.setMessage(message);
        try {
            notificationClient.send(req);
        } catch (Exception e) {
            System.out.println("Notification send failed: " + e.getMessage());
        }
    }
}