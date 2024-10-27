package org.ftf.koifishveterinaryservicecenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class KoiFishVeterinaryServiceCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoiFishVeterinaryServiceCenterApplication.class, args);
    }
}
