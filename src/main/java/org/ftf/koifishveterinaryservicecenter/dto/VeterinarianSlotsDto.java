package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.enums.SlotStatus;

@Getter
@Setter
@Builder
public class VeterinarianSlotsDto {

    @JsonProperty("status")
    private SlotStatus status;

    @JsonProperty("time_slot")
    private TimeSlotDto timeSlot;

    @JsonProperty("veterianrian")
    private User veterinarian;
}
