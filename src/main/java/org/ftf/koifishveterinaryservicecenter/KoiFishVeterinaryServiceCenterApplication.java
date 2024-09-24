package org.ftf.koifishveterinaryservicecenter;

import org.ftf.koifishveterinaryservicecenter.enums.PaymentMethod;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;
import org.ftf.koifishveterinaryservicecenter.model.Payment;
import org.ftf.koifishveterinaryservicecenter.repository.PaymentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class KoiFishVeterinaryServiceCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoiFishVeterinaryServiceCenterApplication.class, args);
    }


}
