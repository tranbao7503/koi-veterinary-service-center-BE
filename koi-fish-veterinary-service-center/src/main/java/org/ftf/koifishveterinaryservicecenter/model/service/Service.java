package org.ftf.koifishveterinaryservicecenter.model.service;

import jakarta.persistence.*;
import lombok.*;
import org.baopen753.koifishveterinaryservice.model.appointment.Appointment;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id",nullable = false)
    private Integer serviceId;

    @Column(name = "service_name", length = 50, nullable = false)
    private String serviceName;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "service_price", nullable = false, precision = 6, scale = 2)   // decimal(6,2)
    private BigDecimal servicePrice;

    // Bidirectional, Identifying relationship
    // Owning side: Appointment
    // Inverse side: Service
    @OneToMany(mappedBy = "service")
    private Set<Appointment> appointments = new LinkedHashSet<>();


}
