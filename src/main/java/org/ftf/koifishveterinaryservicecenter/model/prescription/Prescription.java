package org.ftf.koifishveterinaryservicecenter.model.prescription;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

}
