package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    //    @Lob
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: TimeSlot
    @OneToMany(mappedBy = "timeSlot", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new LinkedHashSet<>();

    // Bidirectional, identifying  relationship
    // Owning side: VeterinarianSlots
    // Inverse side: TimeSlot
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "veterinarian_slots",
            joinColumns = @JoinColumn(name = "slot_id"),
            inverseJoinColumns = @JoinColumn(name = "veterinarian_id")
    )
    private Set<User> veterinarians = new LinkedHashSet<>();

    public LocalDateTime getDateTimeBasedOnSlot() {
        int hour = 0;
        int minute = 0;

        if (this.slotOrder == 1) hour = 8;
        if (this.slotOrder == 2) hour = 10;
        if (this.slotOrder == 3) hour = 13;
        if (this.slotOrder == 4) hour = 15;

        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute);

        return localDateTime;
    }

}

