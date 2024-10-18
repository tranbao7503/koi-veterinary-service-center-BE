package org.ftf.koifishveterinaryservicecenter.dto.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppointmentUpdateDto extends AppointmentBaseDto{

    @JsonProperty("slot_id")
    private Integer slotId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
