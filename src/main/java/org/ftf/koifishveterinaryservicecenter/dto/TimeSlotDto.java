package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.ftf.koifishveterinaryservicecenter.entity.User;

import java.util.LinkedHashSet;
import java.util.Set;

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

    @JsonProperty("appointments")
    private Set<Appointment> appointments;
}
