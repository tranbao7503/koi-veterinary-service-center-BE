package org.ftf.koifishveterinaryservicecenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalReportDto {

    @JsonProperty("report_id")
    private Integer reportId;

    @JsonProperty("veterinarian_id")
    private Integer veterinarianId;

    @JsonProperty("conclusion")
    private String conclusion;

    @JsonProperty("advise")
    private String advise;

    @JsonProperty("prescription_id")
    private Integer prescriptionId;
}
