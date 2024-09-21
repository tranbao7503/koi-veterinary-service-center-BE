package org.ftf.koifishveterinaryservicecenter.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.model.prescription_medicine.PrescriptionMedicine;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id", nullable = false)
    private Integer prescriptionId;

    @Lob
    @Column(name = "instruction", nullable = false)
    private String instruction;

    // Bidirectional, identifying  relationship
    // Owning side: Prescription
    // Inverse side: Medicine
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "prescription_medicine",
            joinColumns = @JoinColumn(name = "prescription_id"),
            inverseJoinColumns = @JoinColumn(name = "medicine_id")
    )
    private Set<Medicine> allMedicine = new LinkedHashSet<>();


}
