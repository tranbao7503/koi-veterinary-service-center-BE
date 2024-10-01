package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {

    @JsonProperty("appointment_id")
    private Integer appointmentId;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("time_slot")
    private TimeSlotDto timeSlot;

}
