package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @JsonProperty("address_id")
    private Integer addressId;

    @JsonProperty("city")
    private String city;

    @JsonProperty("district")
    private String district;

    @JsonProperty("ward")
    private String ward;

    @JsonProperty("home_number")
    private String homeNumber;

    @JsonProperty("status")
    private boolean status;
}
