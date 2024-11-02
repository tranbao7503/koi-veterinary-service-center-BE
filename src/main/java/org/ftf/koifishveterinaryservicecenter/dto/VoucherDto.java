package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VoucherDto {

    @JsonProperty("voucher_id")
    private Integer id;

    @JsonProperty("voucher_code")
    private String voucherCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("quantity")
    private Integer quantity;
}
