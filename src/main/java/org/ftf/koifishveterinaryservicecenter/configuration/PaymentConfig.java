package org.ftf.koifishveterinaryservicecenter.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PaymentConfig {

    @Value("${vnpay.tmn_code}")
    private String tmnCode;

    @Value("${vnpay.hash_secret}")
    private String hashSecret;

    @Value("${vnpay.api_url}")
    private String apiUrl;

    @Value("${vnpay.return_url}")
    private String returnUrl;


}
