package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {

    @JsonProperty("medicine_id")
    private Integer medicineId;    // mapped from Medicine.medicineId

    @JsonProperty("medicine_name")
    private String medicineName;   // mapped from Medicine.medicineName

    @JsonProperty("quantity")
    private Integer quantity;      // mapped from prescription_medicine

}
