package org.ftf.koifishveterinaryservicecenter.dto;

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

    private Integer serviceId;

    @NotBlank
    private String serviceName;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0") // Service price must be at least 0
    private BigDecimal servicePrice;
}
