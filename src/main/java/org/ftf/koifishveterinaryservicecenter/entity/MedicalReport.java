package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "medical_reports")
public class MedicalReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Integer reportId;

//    @Lob
    @Column(name = "conclusion", nullable = true, columnDefinition = "TEXT")
    private String conclusion;


//    @Lob
    @Column(name = "advise", nullable = true, columnDefinition = "TEXT")
    private String advise;


    // Uni-directional, identifying relationship
    // Owning side: MedicalReport
    // Inverse side: User
    @MapsId("veterinarianId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinarian_id", nullable = false, referencedColumnName = "user_id")
    private User veterinarian;


    // Uni-directional, non-identifying
    // Owning side: Medical report
    // Inverse side: Prescription
    @OneToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "prescription_id", nullable = true, referencedColumnName = "prescription_id")
    private Prescription prescription;

}
