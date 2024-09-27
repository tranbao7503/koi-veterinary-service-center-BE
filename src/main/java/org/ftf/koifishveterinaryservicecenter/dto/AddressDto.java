package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @JsonProperty("city")
    private String city;

    @JsonProperty("district")
    private String district;

    @JsonProperty("ward")
    private String ward;

    @JsonProperty("homeNumber")
    private String homeNumber;


}
