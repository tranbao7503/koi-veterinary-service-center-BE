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
public class MovingSurchargeDTO {

    @JsonProperty("moving_surcharge_id")
    private Integer movingSurchargeId;

    @JsonProperty("district")
    @NotBlank(message = "District can not be blank")
    private String district;

    @JsonProperty("price")
    @NotNull(message = "Surcharge price can not be null")
    @DecimalMin(value = "0") // Price must be at least 0
    private BigDecimal price;


}
