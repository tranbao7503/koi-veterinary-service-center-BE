package org.ftf.koifishveterinaryservicecenter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id", nullable = false)
    private Integer feedbackId;

    @Column(name = "rating", nullable = true)
    private Integer rating;

    //  @Lob
    @Column(name = "comment", nullable = true, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "datetime", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime datetime;

    // Bidirectional, identifying relationship
    // Owning side: Feedback
    // Inverse side: Customer
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "user_id")
    private User customer;

    // Bidirectional, identifying relationship
    // Owning side: Feedback
    // Inverse side: Customer
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
    @JsonIgnore
    @OneToOne(mappedBy = "feedback", fetch = FetchType.LAZY)
    private Appointment appointment;

}
