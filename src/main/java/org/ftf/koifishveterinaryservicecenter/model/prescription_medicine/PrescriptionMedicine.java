package org.ftf.koifishveterinaryservicecenter.model.prescription_medicine;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.model.Medicine;
import org.ftf.koifishveterinaryservicecenter.model.Prescription;

@Getter
@Setter
@Entity
@Table(name = "prescription_medicine")
public class PrescriptionMedicine {

    @EmbeddedId
    private PrescriptionMedicineId prescriptionMedicineId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // Bidirectional, identifying  relationship
    // Owning side: PrescriptionMedicine
    // Inverse side: Medicine
    @MapsId("medicineId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_id", nullable = false, referencedColumnName = "medicine_id")
    private Medicine medicine;

    // Bidirectional, identifying  relationship
    // Owning side: PrescriptionMedicine
    // Inverse side: Prescription
    @MapsId("prescriptionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescription_id", nullable = false, referencedColumnName = "prescription_id")
    private Prescription prescription;

}
