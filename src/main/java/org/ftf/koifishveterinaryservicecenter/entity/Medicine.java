package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ftf.koifishveterinaryservicecenter.entity.prescription_medicine.PrescriptionMedicine;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id", nullable = false)
    private Integer medicineId;

    @Column(name = "medicine_name", length = 50, nullable = false)
    private String medicineName;

    // Bidirectional, identifying  relationship
    // Owning side: PrescriptionMedicine
    // Inverse side: Medicine
    @OneToMany(mappedBy = "medicine", orphanRemoval = true)
    private Set<PrescriptionMedicine> prescriptionMedicines;

}
