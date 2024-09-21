package org.ftf.koifishveterinaryservicecenter.model.appointment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.baopen753.koifishveterinaryservice.model.address.Address;
import org.baopen753.koifishveterinaryservice.model.feedback.Feedback;
import org.baopen753.koifishveterinaryservice.model.fish.Fish;
import org.baopen753.koifishveterinaryservice.model.medical_report.MedicalReport;
import org.baopen753.koifishveterinaryservice.model.moving_surcharge.MovingSurcharge;
import org.baopen753.koifishveterinaryservice.model.payment.Payment;
import org.baopen753.koifishveterinaryservice.model.service.Service;
import org.baopen753.koifishveterinaryservice.model.status.Status;
import org.baopen753.koifishveterinaryservice.model.time_slot.TimeSlot;
import org.baopen753.koifishveterinaryservice.model.user.User;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "appointments")
public class Appointment {

    @EmbeddedId
    private AppointmentId appointmentId;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "current_status", nullable = false)
    @ColumnDefault("'PENDING'")
    private String currentStatus;

    @Column(name = "cusomter_name", nullable = false, length = 100)
    private String customerName;

    @Lob
    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "total_price", nullable = false, precision = 6, scale = 2)
    private BigDecimal totalPrice;


    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: Service
    @MapsId("serviceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false, referencedColumnName = "service_id")
    private Service service;


    // Unidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Address
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "address_id", nullable = true, referencedColumnName = "address_id")
    private Address address;


    // Unidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: MovingSurcharge
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "moving_surcharge_id", nullable = true, referencedColumnName = "id")
    private MovingSurcharge movingSurcharge;


    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: TimeSlot
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id", nullable = false, referencedColumnName = "slot_id")
    private TimeSlot timeSlot;


    // Bidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Feedback
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "feedback_id", nullable = true, referencedColumnName = "feedback_id")
    private Feedback feedback;

    // Unidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Report
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "report_id", nullable = true, referencedColumnName = "report_id")
    private MedicalReport medicalReport;

    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: User(customer)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "user_id")
    private User customer;

    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: User(veterinarian)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinarian_id", nullable = false, referencedColumnName = "user_id")
    private User veterinarian;

    // Bidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Fish
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fish_id", nullable = true, referencedColumnName = "fish_id")
    private Fish fish;

    // Bidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Appointment(Follow-up)
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "follow_up_appointment_id", nullable = true, referencedColumnName = "appointment_id")
    private Appointment followUpAppointment;

    // Bidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Appointment(Follow-up)
    @OneToOne(mappedBy = "followUpAppointment")
    private Appointment appointment;

    // Unidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Payment
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "payment_id", nullable = true, referencedColumnName = "payment_id")
    private Payment payment;

    // Bidirectional, identifying relationship
    // Owning side: Status
    // Inverse side: Appointment
    @OneToMany(mappedBy = "appointment")
    private Set<Status> statuses = new LinkedHashSet<>();


}
