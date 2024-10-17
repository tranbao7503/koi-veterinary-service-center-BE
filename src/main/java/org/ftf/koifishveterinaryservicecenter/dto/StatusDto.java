package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;

import java.time.LocalDateTime;

@Data
public class StatusDto {

    @JsonProperty("status_id")
    private Integer statusId;

    @JsonProperty("status")
    private String statusName;

    @JsonProperty("time")
    private LocalDateTime time;

    @JsonProperty("note")
    private String note;

}
