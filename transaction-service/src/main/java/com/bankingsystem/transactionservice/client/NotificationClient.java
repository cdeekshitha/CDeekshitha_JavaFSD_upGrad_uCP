package com.bankingsystem.transactionservice.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications/send")
    void send(@RequestBody NotificationRequest request);

    @Data
    class NotificationRequest {
        private String to;
        private String subject;
        private String message;
    }
}