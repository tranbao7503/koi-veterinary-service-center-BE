package org.ftf.koifishveterinaryservicecenter.email;

import org.ftf.koifishveterinaryservicecenter.service.emailservice.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() throws Exception {
        String to = "crisbrian070503@gmail.com";
        String subject = "OTP Test";
        String message = "OTP is 1234";

        emailService.sendTextEmail(to, subject, message);
    }



}
