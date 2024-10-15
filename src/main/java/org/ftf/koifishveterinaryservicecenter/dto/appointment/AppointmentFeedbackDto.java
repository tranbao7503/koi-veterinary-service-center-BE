package org.ftf.koifishveterinaryservicecenter.dto.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ftf.koifishveterinaryservicecenter.dto.TimeSlotDto;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppointmentFeedbackDto extends AppointmentBaseDto {

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

    @JsonProperty("current_status")
    private AppointmentStatus currentStatus;

}
