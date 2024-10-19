package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.entity.EmailRequest;
import org.ftf.koifishveterinaryservicecenter.service.emailservice.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendNotification")
    public ResponseEntity<?> sendNotification(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendTextEmail(emailRequest.getToEmail(), emailRequest.getSubject(), emailRequest.getMessage());
            return ResponseEntity.ok().body("Send notification successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
