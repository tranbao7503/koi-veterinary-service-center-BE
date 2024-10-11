package org.ftf.koifishveterinaryservicecenter.dto.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentBaseDto {
    @JsonProperty("appointment_id")
    private Integer appointmentId;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;
}
