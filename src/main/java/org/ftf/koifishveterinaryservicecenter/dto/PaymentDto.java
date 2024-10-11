package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("transaction_time")
    private String transactionTime;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("payment_amount")
    private BigDecimal amount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    public PaymentDto(String paymentMethod, BigDecimal amount) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }
}
