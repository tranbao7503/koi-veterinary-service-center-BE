package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SlotStatusDto {
    @JsonProperty("status")
    private String status;
}
