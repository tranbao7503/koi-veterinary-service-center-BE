package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status_name", nullable = false)
    private AppointmentStatus statusName;

    @Column(name = "time", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime time;

    //@Lob --- use columnDefinition = "TEXT" to define type of field in database
    @Column(name = "note", nullable = true, columnDefinition = "TEXT")
    private String note;

    // Bidirectional, identifying relationship
    // Owning side: Status
    // Inverse side: Appointment
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, referencedColumnName = "appointment_id")
    private Appointment appointment;


}
