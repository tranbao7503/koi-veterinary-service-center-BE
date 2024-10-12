package org.ftf.koifishveterinaryservicecenter.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FishDTO {

    @JsonProperty("fish_id")
    private Integer fishId;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("species")
    private String species;

    @JsonProperty("size")
    private Double size;

    @JsonProperty("weight")
    private Double weight;

    @JsonProperty("color")
    private String color;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("customer_id")
    private Integer customerId;

}


