package org.ftf.koifishveterinaryservicecenter.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private Integer serviceId;
    private String serviceName;
    private String description;
    private BigDecimal servicePrice;
}
