package com.bankingsystem.transactionservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String transactionId;
    private String type;
    private Double amount;
    private Instant timestamp;
    private String status;
    private String sourceAccount;
    private String destinationAccount;
}