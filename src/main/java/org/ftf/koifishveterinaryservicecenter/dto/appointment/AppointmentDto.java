package org.ftf.koifishveterinaryservicecenter.dto.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ftf.koifishveterinaryservicecenter.dto.PaymentDto;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppointmentDto extends AppointmentBaseDto {

    @JsonProperty("service_id")
    private Integer serviceId;

    @JsonProperty("address_id")
    private Integer addressId;

    @JsonProperty("veterinarian_id")
    private Integer veterinarianId;

    @JsonProperty("slot_id")
    private Integer slotId;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("phone")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("description")
    private String description;

    @JsonProperty("fish_id")
    private Integer fishId;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("payment")
    private PaymentDto payment;

}
