package com.bankingsystem.notificationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping
    public String sendNotification(@RequestParam String email,
                                   @RequestParam String message) {

        log.info("📩 Notification sent to {} | Message: {}", email, message);
        System.out.println("📩 Notification sent to " + email + " | Message: " + message);

        return "Notification sent to " + email;
    }
}