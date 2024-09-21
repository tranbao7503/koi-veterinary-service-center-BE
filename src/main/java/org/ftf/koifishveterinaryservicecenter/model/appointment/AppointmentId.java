package org.ftf.koifishveterinaryservicecenter.model.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class AppointmentId implements Serializable {
    private static final long serialVersionUID = 2794972907066579511L;

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "veterinarian_id", nullable = false)
    private Integer veterinarianId;

    @Column(name = "slot_id", nullable = false)
    private Integer slotId;

    @Column(name = "service_id", nullable = false)
    private Integer serviceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentId that = (AppointmentId) o;
        return Objects.equals(appointmentId, that.appointmentId) && Objects.equals(customerId, that.customerId) && Objects.equals(veterinarianId, that.veterinarianId) && Objects.equals(slotId, that.slotId) && Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId, customerId, veterinarianId, slotId, serviceId);
    }
}
