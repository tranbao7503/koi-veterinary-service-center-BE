package org.ftf.koifishveterinaryservicecenter.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackDto {

    @JsonProperty("feedback_id")
    private Integer feedbackId;

    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("comment")
    private String comment;

}
