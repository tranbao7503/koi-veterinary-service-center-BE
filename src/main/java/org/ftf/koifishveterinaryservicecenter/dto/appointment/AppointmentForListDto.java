package org.ftf.koifishveterinaryservicecenter.dto.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.ftf.koifishveterinaryservicecenter.dto.TimeSlotDto;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
import org.ftf.koifishveterinaryservicecenter.enums.PaymentStatus;

@Data
public class AppointmentForListDto extends AppointmentBaseDto {

    @JsonProperty("time_slot")
    private TimeSlotDto timeSlot;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("veterinarian_name")
    private String veterinarianName;

    @JsonProperty("appointment_status")
    private AppointmentStatus appointmentStatus;

    @JsonProperty("payment_status")
    private PaymentStatus paymentStatus;

}
