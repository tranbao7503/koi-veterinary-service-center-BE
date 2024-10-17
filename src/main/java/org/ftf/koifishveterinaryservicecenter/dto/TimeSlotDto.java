package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.dto.appointment.AppointmentFeedbackDto;

@Getter
@Setter
@Builder
public class TimeSlotDto {

    @JsonProperty("slot_id")
    private Integer slotId;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("month")
    private Integer month;

    @JsonProperty("day")
    private Integer day;

    @JsonProperty("slot_order")
    private Integer slotOrder;

    @JsonProperty("description")
    private String description;

    @JsonProperty("appointment")
    private AppointmentFeedbackDto appointment;
}
