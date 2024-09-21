package org.ftf.koifishveterinaryservicecenter.model.medical_report;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MedicalReportId {

    @Column(name = "report_id", nullable = false)
    private Integer reportId;

    @Column(name = "veterinarian_id", nullable = false)
    private Integer veterinarianId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalReportId that = (MedicalReportId) o;
        return Objects.equals(reportId, that.reportId) && Objects.equals(veterinarianId, that.veterinarianId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, veterinarianId);
    }
}
