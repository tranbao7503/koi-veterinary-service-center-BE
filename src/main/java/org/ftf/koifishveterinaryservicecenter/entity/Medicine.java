package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id", nullable = false)
    private Integer medicineId;

    @Column(name = "medicine_name", length = 50, nullable = false)
    private String medicineName;

    // Bidirectional, identifying  relationship
    // Owning side: Prescription
    // Inverse side: Medicine
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "prescription_medicine",
            joinColumns = @JoinColumn(name = "medicine_id"),
            inverseJoinColumns = @JoinColumn(name = "prescription_id")
    )
    private Set<Prescription> prescriptions = new LinkedHashSet<>();

}
