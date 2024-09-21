package org.ftf.koifishveterinaryservicecenter.model.time_slot;

import jakarta.persistence.*;
import lombok.*;
import org.baopen753.koifishveterinaryservice.model.appointment.Appointment;

import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "time_slots")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id", nullable = false)
    private Integer slotId;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "day", nullable = false)
    private Integer day;

    @Column(name = "slot_order", nullable = false)
    private Integer slotOrder;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: TimeSlot
    @OneToMany(mappedBy = "timeSlot")
    private Set<Appointment> appointments = new LinkedHashSet<>();

}
