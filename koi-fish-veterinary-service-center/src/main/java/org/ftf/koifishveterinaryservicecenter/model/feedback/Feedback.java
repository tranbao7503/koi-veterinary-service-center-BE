package org.ftf.koifishveterinaryservicecenter.model.feedback;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.baopen753.koifishveterinaryservice.model.appointment.Appointment;
import org.baopen753.koifishveterinaryservice.model.fish.Fish;
import org.baopen753.koifishveterinaryservice.model.user.User;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @EmbeddedId
    private FeedbackId feedbackId;

    @Column(name = "rating", nullable = true)
    private Integer rating;

    @Lob
    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    // Bidirectional, identifying relationship
    // Owning side: Feedback
    // Inverse side: Customer
    @MapsId("customerId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "user_id")
    private User customer;

    // Bidirectional, identifying relationship
    // Owning side: Feedback
    // Inverse side: Customer
    @MapsId("veterinarianId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinarian_id", nullable = false, referencedColumnName = "user_id")
    private User veterinarian;

    // Unidirectional, non-identifying relationship
    // Owning side: Feedback
    // Inverse side: Fish
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fish_id", nullable = true, referencedColumnName = "fish_id")
    private Fish fish;

    // Bidirectional, non-identifying relationship
    // Owning side: Appointment
    // Inverse side: Feedback
    @OneToOne(mappedBy = "feedback")
    private Appointment appointment;

}
