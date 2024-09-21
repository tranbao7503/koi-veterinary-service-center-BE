package org.ftf.koifishveterinaryservicecenter.model.prescription_medicine;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PrescriptionMedicineId {

    @Column(name = "prescription_id", nullable = false)
    private Integer prescriptionId;

    @Column(name = "medicine_id", nullable = false)
    private Integer medicineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrescriptionMedicineId that = (PrescriptionMedicineId) o;
        return Objects.equals(prescriptionId, that.prescriptionId) && Objects.equals(medicineId, that.medicineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescriptionId, medicineId);
    }
}
