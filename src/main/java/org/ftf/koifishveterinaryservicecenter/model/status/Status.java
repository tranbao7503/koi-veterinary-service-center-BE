package org.ftf.koifishveterinaryservicecenter.model.status;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.baopen753.koifishveterinaryservice.model.appointment.Appointment;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "statuses")
public class Status {

    @EmbeddedId
    private StatusId id;

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
    @MapsId("appointmentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, referencedColumnName = "appointment_id")
    private Appointment appointment;


}
