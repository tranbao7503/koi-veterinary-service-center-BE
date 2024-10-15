package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {


    @JsonProperty("service_id")
    private Integer serviceId;

    @JsonProperty("service_name")
    //@NotBlank(message = "service name cannot be blank")
    private String serviceName;

    @JsonProperty("description")
    //@NotBlank(message = "description cannot be blank")
    private String description;

    @JsonProperty("service_price")
    //@NotNull(message = "service price cannot be null")
    @DecimalMin(value = "0") // Service price must be at least 0
    private BigDecimal servicePrice;
}
