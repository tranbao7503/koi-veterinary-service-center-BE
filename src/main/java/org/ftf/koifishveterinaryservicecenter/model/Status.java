package org.ftf.koifishveterinaryservicecenter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "status_name", nullable = false)
    private String statusName;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Lob
    @Column(name = "note", nullable = false)
    private String note;

    // Bidirectional, identifying relationship
    // Owning side: Status
    // Inverse side: Appointment
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, referencedColumnName = "appointment_id")
    private Appointment appointment;


}
