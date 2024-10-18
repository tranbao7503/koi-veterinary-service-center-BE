package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
