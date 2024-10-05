package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {

    @JsonProperty("prescription_id")
    private Integer prescriptionId;

    @JsonProperty("medicines")
    private List<MedicineDto> allMedicine;

    @JsonProperty("instruction")
    private String instruction;    // mapped from prescription
}
