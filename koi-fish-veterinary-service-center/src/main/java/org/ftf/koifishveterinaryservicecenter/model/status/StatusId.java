package org.ftf.koifishveterinaryservicecenter.model.status;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class StatusId {

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusId statusId1 = (StatusId) o;
        return Objects.equals(statusId, statusId1.statusId) && Objects.equals(appointmentId, statusId1.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusId, appointmentId);
    }
}
