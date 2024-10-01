package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ftf.koifishveterinaryservicecenter.enums.AppointmentStatus;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;

    @Column(name = "created_date", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    @ColumnDefault("'PENDING'")
    private AppointmentStatus currentStatus;

    @Column(name = "cusomter_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "email", nullable = true, length = 50, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = true, length = 10)
    private String phoneNumber;

    //    @Lob
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    // Bidirectional, identifying relationship
    // Owning side: Appointment
    // Inverse side: Service
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
    @JoinColumn(name = "moving_surcharge_id", nullable = true, referencedColumnName = "surcharge_id")
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
    @OneToOne(mappedBy = "followUpAppointment", fetch = FetchType.LAZY)
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
    @OneToMany(mappedBy = "appointment", fetch = FetchType.LAZY)
    private Set<Status> statuses = new LinkedHashSet<>();


}
