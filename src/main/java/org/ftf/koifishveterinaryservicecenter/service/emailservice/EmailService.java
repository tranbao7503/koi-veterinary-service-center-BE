package org.ftf.koifishveterinaryservicecenter.service.emailservice;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${sender.email}")
    private String senderEmail;

    @Value("${sender.name}")
    private String senderName;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendTextEmail(String toEmail, String subject, String message) {

        log.info("Sending to email {} with subject {} with message {}", toEmail, subject, message);

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(toEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.setFrom(senderEmail);

            mailSender.send(mailMessage);

            log.info("Email queued");
        } catch (Exception e) {
            log.error("Exception " + e.getMessage());
        }

    }
}
