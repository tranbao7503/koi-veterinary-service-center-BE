package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {

    @JsonProperty("feedback_id")
    private Integer feedbackId;

    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("date_time")
    private LocalDateTime datetime;

    @JsonProperty("appointment")
    private AppointmentDto appointment;

}
