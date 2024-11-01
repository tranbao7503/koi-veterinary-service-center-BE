package org.ftf.koifishveterinaryservicecenter.dto.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.ftf.koifishveterinaryservicecenter.dto.*;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;

import java.math.BigDecimal;

@Data
public class AppointmentDetailsDto extends AppointmentBaseDto {

    @JsonProperty("current_status")
    private AppointmentStatus currentStatus;

    @JsonProperty("customer_name")
    private String customerName;

//    @JsonProperty("slot_id")
//    private Integer slotId;

    @JsonProperty("slot")
    private TimeSlotDto timeSlot;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("description")
    private String description;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("service")
    private ServiceDTO service;

    @JsonProperty("moving_surcharge")
    private MovingSurchargeDTO movingSurcharge;

    @JsonProperty("address")
    private AddressDTO address;

    @JsonProperty("veterinarian")
    private UserDTO veterinarian;

    @JsonProperty("fish")
    private FishDTO fish;

    @JsonProperty("follow_up_appointment_id")
    private Integer followUpAppointmentId;

    @JsonProperty("feedback_id")
    private Integer feedbackId;

    @JsonProperty("discount")
    private BigDecimal discount;
}
