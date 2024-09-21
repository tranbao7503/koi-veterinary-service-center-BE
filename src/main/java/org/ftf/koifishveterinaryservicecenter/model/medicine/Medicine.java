package org.ftf.koifishveterinaryservicecenter.model.medicine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id", nullable = false)
    private Integer medicineId;

    @Column(name = "medicine_name", length = 50, nullable = true)
    private String medicineName;

}
