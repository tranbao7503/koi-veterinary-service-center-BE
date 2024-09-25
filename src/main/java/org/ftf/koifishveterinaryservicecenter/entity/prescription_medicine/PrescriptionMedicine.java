package org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.entity.Medicine;
import org.ftf.koifishveterinaryservicecenter.entity.Prescription;

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
