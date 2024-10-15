package org.ftf.koifishveterinaryservicecenter.entity.veterinarian_slots;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class VeterinarianSlotId {

    @Column(name = "veterinarian_id", nullable = false)
    private Integer veterinarianId;

    @Column(name = "slot_id", nullable = false)
    private Integer slotId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VeterinarianSlotId that = (VeterinarianSlotId) o;
        return Objects.equals(veterinarianId, that.veterinarianId) && Objects.equals(slotId, that.slotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(veterinarianId, slotId);
    }
}
